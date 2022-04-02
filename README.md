# SpringstateMachine

H2 Login:
http://localhost:8761/h2-console


Create Trx :
curl -L -X POST 'http://localhost:8761/transaction/create' -H 'Content-Type: application/json' --data-raw '100'

Change Status
curl -L -X POST 'http://localhost:8761/transaction/change-status' -H 'Content-Type: application/json' --data-raw '{
    "transactionId": 1,
    "newStatus": "APPROVED"
}'
