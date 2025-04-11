# Làm mới accessToken
1) Nhận refreshToken từ client.
2) Xác minh token (định dạng, chữ ký, còn hạn).
3) Kiểm tra jti của refreshToken có trong Redis không.
4) Nếu hợp lệ: Tạo mới accessToken. Nếu hết hạn hoặc không hợp lệ → yêu cầu đăng nhập lại.