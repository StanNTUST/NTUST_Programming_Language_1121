(DEFINE list1 '(1 a 2 b))
(DEFINE list2 '(3 c 4 d))
(DEFINE (inv_app list1 list2)
  (COND
    ((NOT (NULL? list1)) (APPEND (inv_app (CDR list1) list2) (LIST (CAR list1))))
    ((NOT (NULL? list2)) (APPEND (inv_app list1 (CDR list2)) (LIST (CAR list2))))
    (ELSE '())  
  )
)

(inv_app list1 list2)