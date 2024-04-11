import poplib
import smtplib
import email
import time
import dns.resolver
from email.message import EmailMessage
from email.utils import parseaddr

poplib._MAXLINE = 10 * 1024 * 1024  # 10 MB

# Configuration
POP3_SERVER = "pop.gmail.com"
SMTP_SERVER = "smtp.gmail.com"
USERNAME = "jaimengovender@gmail.com"
PASSWORD = "lkdjowpdvycinuht"
CHECK_INTERVAL = 60  # Check every 1 minutes
VACATION_SUBJECT = "I am on vacation"
AUTO_RESPOND_SUBJECT = "prac7"

def check_mx_record(email_address):
    domain = email_address.split('@')[-1]
    try:
        dns.resolver.resolve(domain, 'MX')
        return True
    except dns.resolver.NXDOMAIN:
        return False

def process_emails():
    # Connect to POP3 server and log in
    mailbox = poplib.POP3_SSL(POP3_SERVER, timeout=30)
    mailbox.user(USERNAME)
    mailbox.pass_(PASSWORD)

    # Process messages
    num_messages = len(mailbox.list()[1])
    print(f"Number of messages: {num_messages}")
    responded = set()
    for i in range(1, num_messages + 1):
        raw_email = b'\n'.join(mailbox.retr(i)[1])
        parsed_email = email.message_from_bytes(raw_email)
        from_email = parsed_email['From']
        subject = parsed_email['Subject']

        print(f"Processing email {i}: From: {from_email}, Subject: {subject}")

        # Extract the email address from the 'From' field
        from_email_address = parseaddr(from_email)[1]

        if subject == AUTO_RESPOND_SUBJECT and from_email_address not in responded:
            print(f"Auto-responding to: {from_email_address}")
            if check_mx_record(from_email_address):
                responded.add(from_email_address)
                auto_reply = EmailMessage()
                auto_reply.set_content(VACATION_SUBJECT)
                auto_reply['Subject'] = VACATION_SUBJECT
                auto_reply['From'] = USERNAME
                auto_reply['To'] = from_email_address

                # Send auto-reply
                with smtplib.SMTP_SSL(SMTP_SERVER) as smtp:
                    smtp.login(USERNAME, PASSWORD)
                    smtp.send_message(auto_reply)
            else:
                print(f"MX record not found for {from_email_address}")

    # Close the connection
    mailbox.quit()

# Main loop
while True:
    process_emails()
    time.sleep(CHECK_INTERVAL)
