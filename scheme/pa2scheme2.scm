(DEFINE (dbl_atm atom list2)
  (COND
    ((NULL? list2) '())
    ((LIST?(CAR list2)) 
      (CONS (dbl_atm atom (CAR list2)) (dbl_atm atom (CDR list2))))  
    ((EQ? atom (CAR list2))    
     (CONS atom (CONS atom (dbl_atm atom (CDR list2)))))  
    (ELSE (CONS (CAR list2) (dbl_atm atom (CDR list2))))  
  )
)

(dbl_atm 'a '(a (1 a) (2 b) (3 c) (4 d) (b a g a n o n o)))
