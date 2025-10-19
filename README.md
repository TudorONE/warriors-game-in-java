# warriors-game-in-java
hero movement and fighting implementation using OOP fundamentals


# Obiective

Tema consta in implementarea unui joc de aventura bazat pe text (text adventure game).  
Jocul este reprezentat sub forma unei matrici de dimensiune n×m, unde fiecare celula poate contine diverse elemente, precum inamici sau comori.  
Programul va oferi jucatorului optiuni variate, in functie de tipul de celula in care se afla, oferind permanent suport pentru toate activitatile.  

Jucatorul are posibilitatea de a crea mai multe caractere. La inceputul fiecarui nivel, va alege personajul cu care doreste sa joace.  
Fiecare personaj poseda atribute si abilitati unice, evoluand in timp pe masura ce castiga experienta dupa fiecare eveniment.  
Experienta va fi cuantificata printr-o valoare numerica, iar la atingerea unor praguri stabilite, personajul va avansa in nivel, imbunatatindu-si atributele precum puterea, dexteritatea si carisma.  

Proiectul propune crearea unui astfel de joc folosind principiile programarii orientate pe obiect si functionalitatile puse la dispozitie de limbajul de programare JAVA, aplicand notiunile studiate in cadrul orelor de curs si laborator.

---

## 2. Reguli de joc

Jocul incepe cu crearea unei table de joc, sub forma de matrice, de dimensiuni variabile.  
Caracterul jucatorului va fi plasat intr-o celula aleasa aleatoriu. Continutul celorlalte celule va fi vizibil jucatorului doar in momentul in care caracterul va ajunge pe una dintre celule.  

### Deplasare
Exista patru posibilitati:
- `NORTH` - caracterul se va deplasa in celula de deasupra;
- `SOUTH` - caracterul se va deplasa in celula de dedesubt;
- `WEST` - caracterul se va deplasa in celula din stanga;
- `EAST` - caracterul se va deplasa in celula din dreapta.

### Evenimente la aterizare
- **Inamic**: Caracterul controlat de jucator va lupta cu inamicul.  
  Acesta poate folosi un atac obisnuit sau o abilitate speciala (foc, gheata, pamant). Fiecare actiune are un cost, iar inamicul riposteaza. Daca jucatorul invinge, castiga experienta si isi reincarca partial viata si mana.
- **Sanctuar**: Locul unde caracterul se poate recupera, prin reincarcarea vietii si manei.
- **Portal**: Permite trecerea la urmatorul nivel. Viata si mana personajului sunt resetate la maxim, iar harta se regenereaza.
- **Pustiu**: Celula de tranzit, fara eveniment special.

Jocul se termina doar cand viata caracterului ajunge la 0. Se afiseaza "GAME OVER" si jucatorul revine in meniul principal.

Jucatorul poate alege dintre mai multe personaje. Fiecare personaj are atribute si abilitati unice si evolueaza castigand experienta. La avansarea in nivel, atributele (puterea, dexteritatea, carisma) cresc.

---

## 3. Flow-ul jocului

### 3.1 Initializari
- Jocul incepe cu autentificarea utilizatorului (email si parola). Daca contul exista, se afiseaza lista de personaje.
- Se alege un personaj.
- Se genereaza harta si se pozitioneaza caracterul pe o celula goala.

### 3.2 Loop / Executie continua
- Se afiseaza harta generata in fiecare runda (harta nu se afiseaza in lupta).
- Daca jucatorul se afla pe o celula cu inamic:
  - Se ofera doua optiuni: ataca sau foloseste abilitate.
  - Lupta este pe ture, alternativ.
  - Dupa fiecare atac al jucatorului, inamicul raspunde (atac normal sau abilitate, generat aleator).
  - Dupa infrangerea inamicului, viata se dubleaza pana la maxim, mana se reincarca, si se acorda experienta aleatorie.
- Daca jucatorul se afla pe sanctuar, viata si mana sunt crescute aleator.
- Daca jucatorul se afla pe portal, se acorda puncte de experienta si jocul se reseteaza, incrementand nivelul si numarul de jocuri.
- Daca jucatorul se afla pe celula pustiu, trebuie sa aleaga o noua directie.
- Se citeste urmatoarea mutare si se efectueaza deplasarea.
- Se poate muta si pe celule deja vizitate, care devin pustiu.
- Se repeta pana la final.

### 3.3 Final
- Jocul se termina cand jucatorul paraseste jocul sau viata ajunge la 0.
- Initial, celulele sunt afisate identic; pe masura ce personajul le viziteaza, acestea se descopera.

---

## 4. Arhitectura aplicatiei

### 4.1 Interfete

#### 4.1.1 Battle
Defineste metode comune pentru Character si Enemy:
- `public void receiveDamage(int)`  
  - Inregistreaza pierderea de viata.
  - Exista sansa de 50% de injumatatire a damage-ului, in functie de atributele secundare.
- `public int getDamage()`  
  - Calculeaza damage-ul aplicat.  
  - Exista sansa de 50% de dublare a damage-ului, in functie de atributul principal.  
  - Daca se foloseste abilitate, se adauga damage-ul corespunzator.  

Observatii:
- Atac obisnuit nu consuma mana.
- Formulele damage-ului sunt la alegerea voastra, respectand cerintele de dublare/injumatatire.

### 4.2 Enumerari

#### 4.2.1 CellEntityType
Contine tipurile de celule: `PLAYER`, `VOID`, `ENEMY`, `SANCTUARY`, `PORTAL`.

### 4.3 Clase
Continutul claselor este minimal. Puteti adauga atribute si metode suplimentare.  
Metodele mentionate trebuie sa contina parametrii specificati, dar puteti adauga argumente suplimentare daca ajuta.

---

### 4.3.1 Game
- Lista de conturi: `ArrayList<Account>`
- Harta: `Grid`
- Metoda pentru afisarea optiunilor si preluarea comenzii.
- `public void run()` - incarca datele din JSON si permite alegerea contului si personajului.

---

### 4.3.2 Account
Clasa contine:
- Informatii despre jucator (`Information`).
- Lista cu toate personajele contului (`ArrayList<Character>`).
- Numarul de jocuri jucate de utilizator.

### 4.3.3 Information
Clasa interna clasei `Account`, retine:
- Credentialele jucatorului (`Credentials`).
- Colectie sortata alfabetic cu jocurile preferate.
- Informatii personale despre jucator (nume, tara).

### 4.3.4 Credentials
- Adresa de email a contului.
- Parola asociata adresei de email.

### 4.3.5 Grid
- Extinde `ArrayList<ArrayList<Cell>>`.
- Lungimea si latimea tablei.
- Referinta la caracterul curent (`Character`).
- Celula curenta a caracterului (`Cell`).
- Metoda statica de generare a hărtii:
  - Minim 2 sanctuare, 4 inamici, 1 portal, si pozitia jucatorului.
  - Dimensiune maxima: 10x10.
- 4 metode de deplasare: `goNorth()`, `goSouth()`, `goWest()`, `goEast()`.
  - Daca mutarea nu este posibila, arunca exceptie tratata in clasa `Game`.

### 4.3.6 Cell
- Coordonatele Ox si Oy.
- Tipul celulei (`CellEntityType`).
- Indicator starea celulei (vizitata/nevizitata).

### 4.3.7 Entity
Clasa abstracta care implementeaza interfata `Battle`:
- Lista de abilitati.
- Viata curenta si maxima.
- Mana curenta si maxima.
- Imunitati la atacuri (fire, ice, earth) - 3 valori boolean.
- Metode concrete:
  - Regenerare viata.
  - Regenerare mana.
  - Folosirea unei abilitati:
    - Verifica mana suficienta.
    - Aplica logica atacului si detectarea efectelor abilitatilor.
    - Afiseaza detaliile abilitatii: tip, damage, cost mana.

Observatii:
- Listele de abilitati sunt populate aleator la inceputul luptei (3-6 abilitati).
- Dupa folosirea unei abilitati, aceasta se sterge din lista.
- Clasa `Random` se foloseste pentru generarea numerelor aleatorii.

### 4.3.8 Character
Clasa abstracta care mosteneste `Entity`:
- Numele personajului.
- Experienta curenta (int).
- Nivelul curent (int).
- 3 atribute care influenteaza damage-ul: `Strength`, `Charisma`, `Dexterity`.

Observatii:
- Formulele pentru evolutia atributelor si damage sunt la alegerea voastra, dar trebuie sa fie echilibrate.

### 4.3.9 Warrior / Mage / Rogue
Clase care extind `Character`:
| Caracter | Imunitate | Atribut principal |
|----------|-----------|-----------------|
| Warrior  | Fire      | Strength        |
| Rogue    | Earth     | Dexterity       |
| Mage     | Ice       | Charisma        |

- Metode pentru inregistrarea pierderii de viata si calcularea damage-ului tin cont de atribute.

### 4.3.10 Enemy
- Extinde clasa abstracta `Entity`.
- La instantiare, se seteaza viata, mana, damage-ul normal si imunitati aleator.
- Primește 3-6 abilitati alese aleator.
- Metode pentru pierderea de viata (50% sansa sa evite damage-ul) si calcularea damage-ului (50% sansa dublare).

### 4.3.11 Spell
- Clasa abstracta pentru abilitati.
- Atribute: damage, cost mana.
- Metoda `toString()` pentru afisarea detaliilor abilitatilor.
- Abilitatile sunt generate aleator, cu minim o abilitate de fiecare tip.

### 4.3.12 Ice / Fire / Earth
- Clase care mostenesc `Spell`.
- Apeleaza constructorul superclasei.

---

## 5. Exceptii
- `InvalidCommandException` - comanda invalida.
- `ImpossibleMove` - mutare imposibila (in afara tablei).
- Se pot adauga alte tipuri de exceptii.
- Programarea defensiva este obligatorie pentru a preveni erori.

---

## 6. Fisiere de intrare
- Se foloseste fisierul JSON `accounts.json` pentru detaliile utilizatorilor.
- Se poate folosi clasa existenta pentru parsare sau metoda proprie.
- Daca se foloseste fisierul din arhiva, trebuie schimbata calea catre JSON.
- Metoda de parsare returneaza `ArrayList<Account>` si functioneaza doar daca clasele sunt implementate conform cerintei.
- Se recomanda biblioteca `json-simple` pentru manipularea JSON.

---

## 7. Observatii
- Tipati orice colectie folosita.
- Respectati specificatiile enuntului.
- Puteti adauga alte detalii si functionalitati.
- Modificari la fisierele de input sunt permise doar pentru flaguri utile, fara a modifica datele existente.
- Incapsularea este recomandata, dar nu obligatorie.
- Forumul poate fi folosit pentru intrebari.
- Elementele neprecizate raman la alegerea voastra.

---

## 8. Testare
- Fiecare componenta trebuie testata.
- Nu se puncteaza ierarhia de clase fara teste.
- Se creeaza clasa `Test` cu metoda `main`.
- Metoda `run()` din clasa `Game` va citi si parsa fisierul JSON cu personaje.

### Scenariu de test:
1. Se alege un cont si un personaj din JSON.
2. Se genereaza harta:
   - `P` - player
   - `N` - nevizitata
   - `V` - vizitata / pustiu
   - `S` - sanctuar
   - `E` - enemy
   - `F` - portal
3. Se deplaseaza jucatorul 3 celule la dreapta.
4. Se reincarca viata si mana.
5. Se deplaseaza jucatorul 1 celula la dreapta si 3 celule in jos.
6. In lupta, se folosesc toate abilitatile disponibile; restul sunt atacuri normale.
7. Se deplaseaza jucatorul o celula in jos, ajungand pe celula finala.

Observatii:
- Scenariul este hardcodat cu metodele implementate.
- Implementarea trebuie sa permita generarea hartilor aleator, cu plasarea entitatilor random.
