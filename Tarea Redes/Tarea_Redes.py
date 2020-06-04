import socket
import os

class Aplicacion:

    mensaje = ""
    puerto = 44440

    def __init__(self, mensaje = "XD"):
        self.mensaje = mensaje
    
    def print_mensage(self):
        print (self.mensaje)

class Presentacion:

    pass

class Sesion:

    pass

class Transporte:

    pass

class Red:

    pass

class VinculoDeDato:

    pass

if __name__ == "__main__":

    pru = Aplicacion()
    while True:
        
        pru.print_mensage()
