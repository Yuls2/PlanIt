<?php
    require __DIR__ . '/../vendor/autoload.php';
    use Firebase\JWT\JWT;
    use Firebase\JWT\Key;

    $key = 'example_key';
    $payload = [
        'iss' => 'http://example.org',
        'aud' => 'http://example.com',
        'iat' => 1356999524,
        'nbf' => 1357000000
    ];


    $jwt = JWT::encode($payload, $key, 'HS256');
    try {
      $decoded = JWT::decode($jwt, new Key($key, 'HS256'));
      $decoded_array = (array) $decoded;
      echo $decoded_array['iss'];
    } catch (\Exception $e) { // invalid한 경우
      echo "go away!";
    }

?>
