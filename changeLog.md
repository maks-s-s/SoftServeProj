### ChangeLog
- Implement pagination with current/total page display.
- Set page default to 0 and limit max page number.
- Add admin access check with 403 error handling.
- Session validation on error page; redirect to home if user exists, else login.
- Auto-redirect for login and registration, 403 for other pages.
- Handle user not found error gracefully for login/registration.