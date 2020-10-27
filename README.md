#Setup
Use docker-compose file. It deploys postgres, kafka, zookepper, service on port 8080

#API

Get balance -
`GET /api/wallet?walletId=2 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
cache-control: no-cache


Add transaction - `POST /api/wallet/transaction HTTP/1.1
                   Host: localhost:8080
                   Content-Type: application/json
                   cache-control: no-cache
                   {
                   	"value": 10,
                   	"walletId": 2
                   }`
