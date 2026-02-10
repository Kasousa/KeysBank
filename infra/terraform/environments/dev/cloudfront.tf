# CloudFront Distribution para KeysBank
# URL melhorada + CDN global

resource "aws_cloudfront_distribution" "keysbank" {
  enabled             = true
  is_ipv6_enabled     = true
  comment             = "KeysBank - Frontend + Backend CDN"
  default_root_object = "index.html"
  price_class         = "PriceClass_100" # Mais barato (US, Canada, Europe)

  # Origin: ALB
  origin {
    domain_name = module.alb.alb_dns_name
    origin_id   = "alb-backend"

    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "http-only"
      origin_ssl_protocols   = ["TLSv1.2"]
    }
  }

  # Default behavior: Frontend (SPA)
  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "alb-backend"

    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "allow-all"
    min_ttl                = 0
    default_ttl            = 3600
    max_ttl                = 86400
    compress               = true
  }

  # API behavior: No cache
  ordered_cache_behavior {
    path_pattern     = "/api/*"
    allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "alb-backend"

    forwarded_values {
      query_string = true
      headers      = ["*"]
      cookies {
        forward = "all"
      }
    }

    viewer_protocol_policy = "allow-all"
    min_ttl                = 0
    default_ttl            = 0
    max_ttl                = 0
    compress               = true
  }

  # Restrictions
  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  # SSL Certificate (CloudFront default)
  viewer_certificate {
    cloudfront_default_certificate = true
  }

  # Custom error responses para SPA routing
  custom_error_response {
    error_code         = 403
    response_code      = 200
    response_page_path = "/index.html"
  }

  custom_error_response {
    error_code         = 404
    response_code      = 200
    response_page_path = "/index.html"
  }

  tags = merge(
    local.common_tags,
    {
      Name = "${var.project_name}-cloudfront"
    }
  )
}

# Output da URL do CloudFront
output "cloudfront_url" {
  description = "URL do CloudFront (use esta URL para acessar a aplicação)"
  value       = "https://${aws_cloudfront_distribution.keysbank.domain_name}"
}

output "cloudfront_id" {
  description = "ID da distribuição CloudFront"
  value       = aws_cloudfront_distribution.keysbank.id
}
