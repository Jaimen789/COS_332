import poplib
import email
import argparse
from getpass import getpass

# Configuration
POP3_SERVER = "pop.gmail.com"
USERNAME = "jaimengovender@gmail.com"
PASSWORD = "lkdjowpdvycinuht"

def preview_emails(username, password):
    # Connect to the POP3 server and log in
    mailbox = poplib.POP3_SSL(POP3_SERVER)
    mailbox.user(username)
    mailbox.pass_(password)

    # Get messages list
    num_messages = len(mailbox.list()[1])
    print(f"Number of messages: {num_messages}")  # Debugging statement
    messages = []

    for i in range(1, num_messages + 1):
        raw_email = b'\n'.join(mailbox.top(i, 0)[1])
        parsed_email = email.message_from_bytes(raw_email)
        from_email = parsed_email['From']
        subject = parsed_email['Subject']
        size = mailbox.list()[1][i - 1].split()[-1]  # Modified line
        messages.append((i, from_email, subject, size))

    # Close the connection
    mailbox.quit()

    return messages




def delete_emails(username, password, ids):
    # Connect to the POP3 server and log in
    mailbox = poplib.POP3_SSL(POP3_SERVER)
    mailbox.user(username)
    mailbox.pass_(password)

    for i in ids:
        mailbox.dele(i)

    # Close the connection
    mailbox.quit()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Preview and delete emails from a POP3 mailbox")
    parser.add_argument("-u", "--username", default=USERNAME, help="Email username")
    parser.add_argument("-p", "--password", default=PASSWORD, help="Email password")
    args = parser.parse_args()

    if not args.username or not args.password:
        print("Email credentials required")
        exit(1)

    while True:
        messages = preview_emails(args.username, args.password)
        print("\nEmails in your mailbox:")
        print("ID\tFrom\t\t\tSubject\t\t\tSize")
        for msg in messages:
            print(f"{msg[0]}\t{msg[1]}\t{msg[2]}\t{msg[3]}")

        delete_input = input("\nEnter comma-separated IDs of emails to delete, or type 'q' to quit: ").strip()
        if delete_input.lower() == 'q':
            break

        try:
            ids_to_delete = [int(id.strip()) for id in delete_input.split(',')]
            delete_emails(args.username, args.password, ids_to_delete)
            print(f"{len(ids_to_delete)} email(s) deleted.")
        except ValueError:
            print("Invalid input. Please enter comma-separated IDs.")
