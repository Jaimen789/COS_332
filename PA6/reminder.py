import os
import smtplib
import datetime
from dateutil.parser import parse
from email.mime.text import MIMEText


#u20464348 - Jaimen Govender

# Read the events from the text file
def read_events(file_path):
    events = []
    with open(file_path, 'r') as file:
        for line in file:
            parts = line.strip().split()
            if len(parts) == 2:
                events.append({"date": parse(parts[0], dayfirst=True), "event": parts[1]})
    return events

# Check if the event is in six days
def is_event_in_six_days(event, today):
    event_date = event["date"].date()
    delta = event_date - today
    return delta.days == 6

# Send the email reminder
def send_email(events, sender_email, password):
    receiver_email = sender_email

    message = "Upcoming events in six days:\n\n"

    for event in events:
        message += f"{event['event']} on {event['date'].strftime('%Y-%m-%d')}\n"

    msg = MIMEText(message)
    msg["Subject"] = "Reminder: Upcoming events in six days"
    msg["From"] = sender_email
    msg["To"] = receiver_email

    server = smtplib.SMTP_SSL("smtp.gmail.com", 465)
    server.login(sender_email, password)
    server.sendmail(sender_email, receiver_email, msg.as_string())
    print("Email sent successfully.")
    server.quit()

def main():
    sender_email = "jaimengovender26@gmail.com"
    password = os.environ.get("GMAIL_PASSWORD")

    if not password:
        print("GMAIL_PASSWORD environment variable not set. Exiting.")
        return

    print(f"GMAIL_PASSWORD: {password}")  # For troubleshooting; remove after confirming the password is correct

    today = datetime.datetime.now().date()
    events = read_events("events.txt")

    events_in_six_days = [event for event in events if is_event_in_six_days(event, today)]

    if events_in_six_days:
        print(f"Events in six days: {events_in_six_days}")
        send_email(events_in_six_days, sender_email, password)
    else:
        print("No events in six days.")

if __name__ == "__main__":
    main()
#Python Email Reminder
# Password fxrwbqyxwdtolwvu