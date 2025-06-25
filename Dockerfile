FROM python:3.10-slim-buster
LABEL maintainer="core.platform@nexmo.com"

WORKDIR /app

# Copy only requirements.txt first (helps with Docker layer caching)
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Then copy the rest of the app
COPY . .

# Run the app
CMD ["python3", "-m", "app.validate_phone"]