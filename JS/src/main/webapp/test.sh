curl -i -X GET http://localhost:8080/test
# curl -i -X POST -d username=s -d password=s -L http://localhost:8080/login
# curl -i -X POST 'http://localhost:8080/login' -H 'Content-Type: application/x-www-form-urlencoded' -H 'Cookie: JSESSIONID=6FAFC9D4805B802C07E53B44AA87BCBD' -F username=s -F password=s
# curl -i -X GET 'http://localhost:8080/login' -H 'Content-Type: application/x-www-form-urlencoded' -F username=s -F password=s
# curl -i -X POST 'http://localhost:8080/login' -H 'Content-Type: application/x-www-form-urlencoded' -F "username=s" -F "password=s" --data-urlencode "_csrf=CSRF_TOKEN"
