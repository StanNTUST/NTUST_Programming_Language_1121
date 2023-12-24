mother(X, Y) :- parent(X,Y), female(X).
father(X, Y) :- parent(X,Y), male(X).
daughter(X, Y) :- parent(Y,X), female(X).
son(X, Y) :- parent(Y,X), male(X).
child(X, Y) :- parent(Y,X).
sister(X, Y) :- parent(P,X), parent(P,Y), female(X).
brother(X, Y) :- parent(P,X), parent(P,Y), male(X).
sibling(X, Y) :- parent(P,X), parent(P,Y).
uncle(X, Y) :- parent(P,Y), brother(X, P).
aunt(X, Y) :- parent(P,Y), sister(X, P).
cousin(X, Y) :- parent(P,Y), sibling(P2, P), P \= P2, parent(P2,X).


male(dylan).
male(ronnie).
male(mark).
male(mel).
male(richard).
female(annie).
female(leo).
female(haru).
female(janny).
female(joan).
female(rose).
parent(annie, janny).
parent(annie, richard).
parent(annie, joan).
parent(janny, leo).
parent(janny, ronnie).
parent(joan, haru).
parent(mark, janny).
parent(mark, richard).
parent(mel, joan).
parent(richard, dylan).
parent(richard, rose).

