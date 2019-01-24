# sample-phonehint-otpfill

Uses 
* HintRequest for suggesting Phone numbers from Google Account.
* SMS Retriever API for fetching the SMS and auto filling the OTP

# How to generate the App Hash
```
keytool -exportcert -alias androiddebugkey -keystore debug.keystore | xxd -p | tr -d "[:space:]" | echo -n com.harishannam.autootpread `cat` |
sha256sum | tr -d "[:space:]-" | xxd -r -p | base64 | cut -c1-11
```
