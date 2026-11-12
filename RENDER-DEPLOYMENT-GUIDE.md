# MSSD Backend — Render Deployment Guide

Deploy the Spring Boot backend on Render by connecting your GitHub repo. Render builds and deploys automatically on every push.

## Prerequisites

- A GitHub repo containing this code, pushed and up to date.
- A free [Render account](https://dashboard.render.com/register).
- No local Docker needed — Render builds the Dockerfile for you.

## What gets deployed

| Resource      | Type      | Plan | Purpose                          |
|---------------|-----------|------|----------------------------------|
| `mssd-db`     | PostgreSQL | Free | Managed database (free 90 days)  |
| `mssd-backend`| Web Service | Free | Spring Boot REST API (public)    |

Database credentials are auto-injected as environment variables — you never type a password.

> **Why Docker?** Render does not have a native Java runtime. All Java/Spring Boot apps on Render deploy via Dockerfile. You don't interact with Docker yourself — Render builds the Dockerfile automatically from your repo on every push.

---

## Deploy via Blueprint (recommended)

The `render.yaml` at the repo root is a Render Blueprint that defines the database + web service.

1. **Push your code to GitHub.**
2. Go to **Dashboard → New → Blueprint**.
3. Select your repository.
4. Render detects `render.yaml` and shows the database + web service.
5. Click **Apply**.
6. Wait for the build to finish (first build takes 3–5 minutes).
7. Your API is live at `https://mssd-backend-xxxx.onrender.com`

### After first deploy

1. **Update CORS** — in the Render dashboard, edit the `mssd-backend` service's environment variables. Change `CORS_ALLOWED_ORIGINS` to your frontend's actual URL (e.g. `https://mssd-frontend.onrender.com`). Multiple origins are comma-separated.
2. **Update frontend API URL** — point your Angular frontend's API base URL to the backend's `onrender.com` address.

---

## Deploy manually (without Blueprint)

### 1. Create the PostgreSQL database

1. **Dashboard → New → PostgreSQL**.
2. Name it `mssd-db`, select the **Free** plan.
3. Click **Create Database**.
4. Note the internal host, port, user, password, and database name from the dashboard.

### 2. Create the backend web service

1. **Dashboard → New → Web Service**.
2. Connect your GitHub repository.
3. Set **Language** to **Docker**.
4. Set **Dockerfile Path** to `./mssd-backend/Dockerfile`.
5. Set **Docker Build Context** to `./mssd-backend`.
6. Set **Health Check Path** to `/actuator/health`.
7. Add environment variables:

   | Key | Value |
   |-----|-------|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `PGHOST` | *(from database — Internal Host)* |
   | `PGPORT` | *(from database — Port, usually 5432)* |
   | `PGUSER` | *(from database — Username)* |
   | `PGPASSWORD` | *(from database — Password)* |
   | `PGDATABASE` | *(from database — Database name)* |
   | `CORS_ALLOWED_ORIGINS` | your frontend URL |
   | `APP_UPLOAD_DIR` | `/app/uploads` |

   > Tip: Instead of typing the database values, you can link the database to the service in the Render dashboard — Render auto-fills the connection variables.

8. Click **Create Web Service**.

---

## Important notes

### File uploads (ephemeral storage)
On the **free** tier, the filesystem is ephemeral — uploaded files are lost on every redeploy. To persist them, upgrade the backend to **Starter** and uncomment the `disk` section under `mssd-backend` in `render.yaml` (mount path `/app/uploads`).

### Database seeding
The app auto-seeds initial data (categories, themes, formations, company info, highlights, calendar events) on first start when the database is empty — no manual SQL needed.

### Connecting from the frontend
Your backend's public URL looks like `https://mssd-backend-xxxx.onrender.com`. Update the frontend's environment/API configuration to use this URL instead of `http://localhost:8080`.

### Free tier limitations
- The web service **spins down after 15 minutes of inactivity** and cold-starts on the next request (~30–60 seconds).
- No persistent disks (uploads are lost on redeploy).
- PostgreSQL free tier expires after 90 days (then ~$1/month).
- 750 free instance hours/month.

### Using an external database
To use an external PostgreSQL or MySQL database instead of Render's, delete the `databases` section from `render.yaml`, then set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` in the backend's environment variables. Spring auto-detects the driver from the JDBC URL.

---

## Troubleshooting

| Issue | Fix |
|-------|-----|
| Build fails — Maven can't download deps | Ensure `dockerContext` is `./mssd-backend` so `pom.xml` is in the build context |
| App starts but DB connection refused | Wait for `mssd-db` to finish provisioning (check its logs); verify PGHOST/PGPORT are set |
| CORS errors in browser | Set `CORS_ALLOWED_ORIGINS` to match your frontend's exact URL |
| Health check failing | Confirm `/actuator/health` returns 200 (actuator endpoints are exposed in prod config) |
| Port mismatch | The app reads the `PORT` env var (set by Render). No action needed. |
