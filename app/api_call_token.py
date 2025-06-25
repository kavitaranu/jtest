import requests
import sys

def validate_phone_number(phone_number):
    url = "https://api-eu.dev.v1.vonagenetworks.net/auth/token"  # <-- replace with your actual API
    token = "YOUR_BEARER_TOKEN"  # <-- you can also read this from env or a secure store

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    payload = {
        "phone_number": phone_number
    }

    try:
        response = requests.post(url, json=payload, headers=headers)
        response.raise_for_status()
        data = response.json()
        print("API Response:", data)
    except requests.exceptions.RequestException as e:
        print("API call failed:", e)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 validate_phone.py <phone_number>")
        sys.exit(1)

    phone = sys.argv[1]
    validate_phone_number(phone)