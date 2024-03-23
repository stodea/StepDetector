# motion_server.py
import socket
import pyautogui
import time
from pynput.keyboard import Key, Controller
import csv
from datetime import date
from datetime import time
from datetime import datetime
from datetime import timedelta

HOST = '0.0.0.0'  # listen on all available interfaces
PORT = 12345

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((HOST, PORT))
server_socket.listen(1)

print(f"Listening on {HOST}:{PORT}...")

keyboard = Controller()

signals = 1
FMT = "%H:%M:%S:%f"
tdelta_zero = timedelta(hours = 0, minutes = 0, seconds = 0, milliseconds = 0) 
tdelta = timedelta(hours = 0, minutes = 0, seconds = 0, milliseconds = 0)
tmovement = timedelta(hours = 0, minutes = 0, seconds = 0, milliseconds = 600)
client_socket, addr = server_socket.accept()
print(f"Connection from {addr}")
data = client_socket.recv(1024)
key1 = data.decode()
print(f"{signals} - movement detected - {key1}")     

while True:        
    while tdelta < tmovement:  
        if tdelta > tdelta_zero:
            # pyautogui.keyDown('up')         
            keyboard.press(Key.up)
            print(f"{Key.up} pressed")     
            with open('movement_times.txt', 'a') as f:
                f.write(f"\n{key1} {key2} {tdelta}")   
                print(f"{signals} - movement time passed - {tdelta}")         
 
        client_socket, addr = server_socket.accept()
        # print(f"Connection from {addr}")
        data = client_socket.recv(1024)
        key2 = data.decode()
        print(f"{signals} - movement detected - {key2}")     

        tdelta = datetime.strptime(key2, FMT) - datetime.strptime(key1, FMT)
        key1 = key2        
    
        signals += 1 
        client_socket.close()

    keyboard.release(Key.up)
    print(f"{Key.up} released")    
    tdelta = timedelta(hours = 0, minutes = 0, seconds = 0, milliseconds = 0)
    # keyboard.release(Key.up)
    # pyautogui.keyUp('up')         
        # keyboard.press(Key.up)
    
        # key_start = 0
        # key_end = 40
        # while key_start < key_end:
        #     time.sleep(0.025)
        #     keyboard.press(Key.up)
        #     key_start += 1    
        
        
    
