echo "Building app..."
./mvnw clean package -DskipTests

echo "Deploy files to server..."
scp -r target/be.jar root@222.255.238.187:/var/www/be/

ssh root@222.255.238.187 <<EOF
# Tìm PID của tiến trình đang sử dụng cổng 8080
pid=\$(sudo lsof -t -i:8080)

# Kiểm tra xem tiến trình có đang chạy trên cổng 8080 hay không
if [ -z "\$pid" ]; then
    echo "No process using port 8080. Starting server..."
else
    echo "Process using port 8080 found with PID \$pid. Restarting server..."
    sudo kill -9 "\$pid"
    sleep 2  # Đợi một chút để tiến trình dừng hoàn toàn
fi

# Điều hướng tới thư mục chứa ứng dụng
cd /var/www/be

# Khởi động lại server
echo "Starting server on port 8080..."
nohup java -jar be.jar > be.log 2>&1 &

# Thông báo server đã khởi động
echo "Server restarted successfully."
EOF

exit
echo "Done!"
