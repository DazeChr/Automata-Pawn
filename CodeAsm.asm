.MODEL SMALL
.586
.STACK 100h
; variables
.DATA
prom DW 0
aaaa DB "$"
tempString DB 15 DUP('$')

ten DW 10
tempInt DW ?

.CODE
.STARTUP
MOV prom, 3
MOV AX, prom
CALL IntToStr
MOV AH, 9H
LEA DX, OFFSET aaaa
INT 21H
MOV AH, 9
LEA DX, OFFSET tempString
INT 21H
.EXIT

 IntToStr PROC NEAR
     MOV tempInt, AX
     CALL WordToStr
     RET
 IntToStr ENDP

 WordToStr PROC NEAR
     PUSH AX
     PUSH DX
     PUSH CX
     MOV AX, tempInt  ; Cargamos el valor de tempInt en AX
     MOV CX, 0
     MOV BX, OFFSET tempString
     ADD BX, 14  ; Empezamos desde el final de tempString
 NEXT_DIGIT:
     XOR DX, DX
     DIV ten
     ADD DL, '0'
     MOV [BX], DL
     DEC BX
     INC CX
     TEST AX, AX
     JNZ NEXT_DIGIT
     INC BX  ; Ajustamos BX para apuntar al primer dígito
     MOV SI, BX
     MOV DI, OFFSET tempString
 MOVE_DIGITS:
     MOV AL, [SI]
     MOV [DI], AL
     INC SI
     INC DI
     LOOP MOVE_DIGITS
     MOV BYTE PTR [DI], '$'
     POP CX
     POP DX
     POP AX
     RET
 WordToStr ENDP
END
