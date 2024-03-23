# motion_server.py
import socket
from pynput.keyboard import Key, Controller

HOST = '0.0.0.0'  # listen on all available interfaces
PORT = 12345

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((HOST, PORT))
server_socket.listen(1)

print(f"Listening on {HOST}:{PORT}...")

keyboard = Controller()

while True:
    client_socket, addr = server_socket.accept()    
    data = client_socket.recv(1024)
    if data:
        signal = data.decode()        
        if signal.count("press"):
            keyboard.press(Key.up)                        
        elif signal.count("release"):
            keyboard.release(Key.up)            
        # elif signal.count("period"):
        #     pass
        print(str(addr[1]) + " " + signal)
    client_socket.close()
