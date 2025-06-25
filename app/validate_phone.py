import requests
import sys

def validate_phone_number(phone_number):
    # Dummy internal API endpoint (replace with actual later)
    url = "https://api-eu.dev.v1.vonagenetworks.net/v1/validate-phone"

    # Simulated token (replace with real token when available)
    token = "test_dummy_token_123"

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    # Simulated payload structure
    payload = {
        "phone_number": phone_number
    }

    try:
        print(f"Sending request to {url} with phone number {phone_number}")
        response = requests.post(url, json=payload, headers=headers)
        print("Status Code:", response.status_code)

        # If you expect JSON back
        try:
            data = response.json()
            print("API Response:", data)
        except ValueError:
            print("Non-JSON response received.")
            print(response.text)

    except requests.exceptions.RequestException as e:
        print("API call failed:", e)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 validate_phone.py <phone_number>")
        sys.exit(1)

    phone = sys.argv[1]
    validate_phone_number(phone)