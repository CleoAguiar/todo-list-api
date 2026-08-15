# todo-list-api

Build a RESTful API to allow users to manage their to-do list.

## 🐳 Docker

### Build the image

```bash
docker build -t todo-list-api .
```

### Run the container

```bash
docker run \
  --name todo-list-api-container \
  -p 8080:8080 \
  -e JWT_SECRET="your-secret-key" \
  todo-list-api
```

### PowerShell

```powershell
docker run `
  --name todo-list-api-container `
  -p 8080:8080 `
  -e JWT_SECRET="your-secret-key" `
  todo-list-api
```

The API will be available at:

```text
http://localhost:8080
```

### Environment variables

| Variable | Description | Required |
| --- | --- | --- |
| `JWT_SECRET` | Secret key used to sign and validate JWT tokens | Yes |
