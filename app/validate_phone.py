import requests
import sys

def validate_phone_number(phone_number):
    # Dummy internal API endpoint for GET
    url = "https://httpbin.org/get"

    # Simulated token
    token = "test_dummy_token_123"

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    # Query parameters instead of JSON body
    params = {
        "phone_number": phone_number
    }

    try:
        print(f"Sending GET request to {url} with phone number {phone_number}")
        response = requests.get(url, headers=headers, params=params)
        print("Status Code:", response.status_code)

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