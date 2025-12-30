# Deploy VivaahLok to Render

## Prerequisites
- GitHub account with your code pushed to a repository
- Render account (free tier available)
- MongoDB Atlas or Render MongoDB (optional)

## Step 1: Push Code to GitHub

1. Initialize Git repository (if not already done):
```bash
git init
git add .
git commit -m "Initial commit with Firebase authentication"
```

2. Create a new repository on GitHub
3. Add remote and push:
```bash
git remote add origin https://github.com/yourusername/vivahlok.git
git branch -M main
git push -u origin main
```

## Step 2: Deploy to Render

### Option 1: Using render.yaml (Recommended)

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click "New" -> "Web Service"
3. Connect your GitHub repository
4. Render will automatically detect the `render.yaml` file
5. Review and create the services

### Option 2: Manual Setup

1. **Web Service Setup**:
   - Go to Render Dashboard → New → Web Service
   - Connect your GitHub repository
   - Environment: Docker
   - Build Context: ./
   - Dockerfile Path: ./Dockerfile
   - Add Environment Variables (see below)

2. **Database Setup** (Optional):
   - Go to Render Dashboard → New → PostgreSQL/MongoDB/Redis
   - Choose your database service
   - Note the connection string

## Step 3: Configure Environment Variables

Add these environment variables in your Render web service:

### Required Variables:
- `SPRING_DATA_MONGODB_URI` - MongoDB connection string
- `JWT_SECRET` - Your JWT secret key
- `FIREBASE_CREDENTIALS_FILE` - Path to Firebase service account file
- `FIREBASE_DATABASE_URL` - Firebase database URL

### Optional Variables:
- `SPRING_DATA_REDIS_HOST` - Redis host (if using Redis)
- `SPRING_DATA_REDIS_PORT` - Redis port (default: 6379)
- `JWT_EXPIRATION` - JWT expiration time (default: 86400000)
- `JWT_REFRESH_EXPIRATION` - Refresh token expiration (default: 604800000)
- `TWILIO_SID` - Twilio account SID (for SMS)
- `TWILIO_AUTH` - Twilio auth token
- `TWILIO_NUMBER` - Twilio phone number

## Step 4: Firebase Configuration

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use existing one
3. Go to Project Settings → Service Accounts
4. Click "Generate new private key"
5. Upload the JSON file to your Render service or set the content as environment variable

## Step 5: Deployment

Once you've configured everything:
1. Push your changes to GitHub
2. Render will automatically deploy your application
3. Monitor the deployment logs for any issues

## Step 6: Access Your Application

After successful deployment:
- Your API will be available at: `https://your-app-name.onrender.com`
- Swagger UI: `https://your-app-name.onrender.com/swagger-ui.html`
- Health check: `https://your-app-name.onrender.com/actuator/health`

## Troubleshooting

### Common Issues:

1. **Build Failures**:
   - Check the deployment logs
   - Ensure all dependencies are in pom.xml
   - Verify Dockerfile syntax

2. **Database Connection Issues**:
   - Verify MongoDB connection string
   - Check if database is running
   - Ensure proper credentials

3. **Firebase Issues**:
   - Verify Firebase service account file
   - Check Firebase project settings
   - Ensure proper database URL

4. **Port Issues**:
   - Render uses dynamic PORT environment variable
   - Application should bind to PORT from environment

### Monitoring:
- Check Render logs for real-time monitoring
- Use `/actuator/health` endpoint for health checks
- Monitor resource usage in Render dashboard

## Production Considerations:

1. **Security**:
   - Use strong secrets for JWT
   - Enable HTTPS (Render does this automatically)
   - Validate all inputs

2. **Performance**:
   - Monitor response times
   - Consider using CDN for static files
   - Optimize database queries

3. **Scaling**:
   - Render supports auto-scaling
   - Monitor resource usage
   - Consider upgrading plan for production

## Support:

- Render Documentation: https://render.com/docs
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Firebase Documentation: https://firebase.google.com/docs
