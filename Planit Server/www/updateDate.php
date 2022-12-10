<?php header("Content-Type:text/html;charset=utf-8"); ?>
<?php
    require __DIR__ . '/../vendor/autoload.php';
    use Firebase\JWT\JWT;
    use Firebase\JWT\Key;

    // 토큰 header 양식 [authorization] => Bearer eyJ0eXAiOi...
    $headers = apache_request_headers();
    $authHeader =isset($headers['authorization'])?$headers['authorization']:null;

    $host = 'localhost';
    $user = 'planit2022';
    $password = '------';
    $dbName = 'planit2022';

    header('Content-Type: application/json');

    $conn = new mysqli($host, $user, $password, $dbName);
    if(!($conn)){
        echo "db 연결 실패: " . mysqli_connect_error();
    }
    /* ======================== */

    $sql = "UPDATE CHECKITEM SET IsChecked = 0;";
    $stmt = $conn->prepare($sql);
    // $stmt->bind_param("iis",$addGrowth,$groupID,$targetUserID);
    $result = $stmt->execute();

    $stmt->close();
    mysqli_close($conn);
?>
