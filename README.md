# Space Pictionary
<img width="1819" alt="Screenshot 2025-05-09 at 8 07 38 AM" src="https://github.com/user-attachments/assets/637efc5e-fb42-4331-a86a-4a79aede4e1f" />

This project was made to be demoed as a network game. I wanted something the class could play while I presented the project.
After dealing with java webservers (the worst webserver api I have ever used) I decided to with pictionary because it would be uneffected
by internet latency.

The backend uses the default Java 17 HTTP Server (Which like I said, I do not recomend using).
The Frontend uses a basic html page. Websockets were not supported in Java 17, which is all I had on our school computers.
So my solution was to ping the server every 100 miliseoncds (It started tweaking out if I tried any faster) to get the game data, like
the pictionary canvas data.
