Ce projet est un **simulateur de systèmes d'exploitation** développé en Java 11. Il permet de modéliser et d’observer le comportement de processus dans un environnement simulé, en prenant en compte les politiques d’ordonnancement CPU, la gestion de la mémoire, et la gestion du disque.

---

## 🎯 Objectifs

- Simuler plusieurs algorithmes d'**ordonnancement de processus** (FIFO, Tourniquet, Priorité).
- Implémenter une gestion réaliste des **événements** système : `CALCUL`, `LECTURE`, `ECRITURE`, `DORMIR`, `FIN`.
- Gérer les blocages en **mémoire** (pagination) et en **disque** (bras, pistes).
- Produire une **trace d’exécution temporelle** et des **statistiques**.

---

## ▶️ Exécution

### Compilation (avec Maven)
```bash
mvn clean package
java -cp target/classes fr.ul.miashs.jase.Main config.txt programmes.txt

---

## Exemple de sortie 

--- TRACE D'EXECUTION ---
[0 ms] CPU: P1
[10 ms] CPU: P1
[20 ms] CPU: P2
[30 ms] IDLE
...

--- STATISTIQUES ---
Nb processus terminés : 3
Délai de rotation moyen : 540,00 ms
Réactivité moyenne : 30,00 ms

--- DÉTAIL PAR PROCESSUS ---
P1 | arrivée=0 | début=0 | fin=60 | rotation=60
...