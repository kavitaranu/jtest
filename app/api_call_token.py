import requests
def validate_phone_number(phone_number):
    api_key = "YOUR_API_KEY"
    url = "http://apilayer.net/api/validate"

    params = {
        "access_key": api_key,
        "number": phone_number,
        "format": 1
    }

    try:
        response = requests.get(url, params=params)
        response.raise_for_status()
        data = response.json()
        print("Full API response:", data)  # debug print here

        if data.get("valid"):
            print("Phone number is valid.")
            print("Country:", data.get("country_name"))
            print("Carrier:", data.get("carrier"))
            print("Line Type:", data.get("line_type"))
        else:
            print("Invalid phone number.")

    except requests.exceptions.RequestException as e:
        print("API call failed:", e)

if __name__ == "__main__":
    phone = input("Enter phone number (with country code, e.g. +14158586273): ")
    validate_phone_number(phone)