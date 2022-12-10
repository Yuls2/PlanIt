<?php header("Content-Type:text/html;charset=utf-8"); ?>
<?php
    $param = $_POST['parameter'];

    $host = 'localhost';
    $user = 'planit2022';
    $password = '------';
    $dbName = 'planit2022';

    header('Content-Type: application/json');

    $conn = new mysqli($host, $user, $password, $dbName);
    if(!($conn)){
        echo "db 연결 실패: " . mysqli_connect_error();
    }

    $sql = "SELECT name FROM TEST WHERE tid = ?;";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i",$param);
    $stmt->execute();
    $result = $stmt->get_result();
    $output = new stdClass;

    if(mysqli_num_rows($result) > 0){ // 쿼리 결과로 1행 이상 존재한다면
        if($row = mysqli_fetch_assoc($result)) {
                $output -> result = 1;
                $output -> name = $row['name'];
        }
    } else{
              $output -> result = 0;
    }

    echo json_encode($output);
    $stmt->close();
    mysqli_close($conn);
?>
