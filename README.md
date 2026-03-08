# 🧪 POC Java pour des tests de performance sur des modèles de conception frontend

> **Projet réalisé dans le cadre d'un mémoire ADS (Application à la Démarche Scientifique)**  
> ⚠️ **Note sur l'IA** : L'intégralité du code de ce projet a été générée par Claude (Anthropic) à partir de spécifications rédigées par Benjamin REPELLIN, l'auteur du mémoire. L'usage de l'IA est documenté dans ce mémoire dans une démarche de transparence scientifique. Le code a été relu, validé et adapté par l'auteur.

---

## 📋 Table des matières

- [Contexte](#-contexte)
- [Objectif du POC](#-objectif-du-poc)
- [Architecture du projet](#-architecture-du-projet)
- [Les 5 patterns implémentés](#-les-5-patterns-implémentés)
- [Prérequis](#-prérequis)
- [Installation et lancement](#-installation-et-lancement)
- [Mesure des performances](#-mesure-des-performances)
- [Structure des fichiers](#-structure-des-fichiers)
- [Remarques méthodologiques](#-remarques-méthodologiques)

---

## 📚 Contexte

Ce dépôt contient le code source d'un **Proof of Concept (POC)** développé pour un mémoire universitaire en informatique. Le mémoire compare les principaux modèles de conception (*design patterns*) utilisés dans le développement frontend Java, selon des critères de performance, de maintenabilité et de couplage architectural.

Les patterns étudiés sont : **MVC**, **MVP**, **MVVM**, **MVI** et **VIPER**.

---

## 🎯 Objectif du POC

L'objectif est de comparer les 5 patterns sur une **base strictement équivalente** :

- **Même technologie frontend** : Java Swing
- **Même interface graphique** : dimensions, textes, boutons et layout identiques pour tous les patterns
- **Même backend** : une architecture hexagonale partagée (port & adapter), indépendante du pattern frontend
- **Mêmes fonctionnalités** : saisie de deux opérandes, sélection d'une opération, calcul (appel backend), reset (modification du modèle local)
- **Même séquence de test** : 6 calculs → 1 reset

Chaque pattern loggue le **temps de réponse en microsecondes** pour chaque action, ce qui permet de comparer les overheads architecturaux.

---

## 🏗️ Architecture du projet

### Vue d'ensemble

```
┌────────────────────────────────────────────────────────────┐
│                         FRONTEND                           │
│  ┌───────┐  ┌───────┐  ┌────────┐  ┌───────┐   ┌───────┐   │
│  │  MVC  │  │  MVP  │  │  MVVM  │  │  MVI  │   │ VIPER │   │
│  └───┬───┘  └───┬───┘  └───┬────┘  └───┬───┘   └───┬───┘   │
│      └──────────┴──────────┴───────────┴───────────┘       │
│                            │                               │
│                    CalculatorAdapter                       │
│               (Port & Adapter — hexagonal)                 │
└────────────────────────────┬───────────────────────────────┘
                             │
┌────────────────────────────▼───────────────────────────────┐
│                         BACKEND                            │
│  CalculatorUseCase (port)  ←→  CalculatorService (domaine) │
└────────────────────────────────────────────────────────────┘
```

### Backend hexagonal

Le backend suit une **architecture hexagonale** (Ports & Adapters) :

| Classe | Rôle |
|--------|------|
| `CalculatorUseCase` | Port d'entrée — interface définissant le contrat métier |
| `CalculatorService` | Service domaine — implémentation pure (add, subtract, multiply) |
| `CalculatorAdapter` | Adapter driving — point d'entrée exposé au frontend |

Les calculs sont volontairement simples (O(1)) pour que la latence mesurée reflète uniquement l'overhead architectural du pattern frontend, et non le backend.

### Classe commune

| Classe | Rôle |
|--------|------|
| `ViewConstants` | Constantes partagées par toutes les vues (dimensions, textes, layout Swing) |

---

## 🔷 Les 5 patterns implémentés

### MVC — Model View Controller

```
View ──observe──► Model ◄──écrit── Controller ──lit──► View
```

- Le **Model** notifie la View via un pattern Observer intégré (`addChangeListener`)
- Le **Controller** lit la View et écrit le Model
- La View se rafraîchit automatiquement à chaque changement du Model

**Fichiers** : `CalculatorModel.java`, `CalculatorView.java`, `CalculatorController.java`

---

### MVP — Model View Presenter

```
View ──délègue──► Presenter ──lit/écrit──► Model
View ◄──MAJ manuelle── Presenter
```

- Le **Model** est un POJO passif (aucun observer)
- Le **Presenter** pilote la View via une interface (`CalculatorViewContract`)
- La mise à jour de la View est **manuelle** depuis le Presenter
- Le Presenter est 100% testable sans Swing (interface mockable)

**Fichiers** : `CalculatorModel.java`, `CalculatorViewContract.java`, `CalculatorView.java`, `CalculatorPresenter.java`

---

### MVVM — Model View ViewModel

```
View ──binding──► ObservableProperty ◄──set()── ViewModel ──► Model
```

- Le **ViewModel** expose des `ObservableProperty<T>` (data binding maison)
- La **View** s'y abonne via `bind()` : une seule déclaration, mise à jour automatique
- Le ViewModel ne connaît pas la View (zéro couplage)
- Simule le mécanisme de JavaFX `Property` / Android `LiveData`

**Fichiers** : `CalculatorModel.java`, `ObservableProperty.java`, `CalculatorViewModel.java`, `CalculatorView.java`

---

### MVI — Model View Intent

```
View ──dispatch(Intent)──► Store(Reducer) ──new State──► View.render(state)
```

- Flux **strictement unidirectionnel** (inspiré de Redux/Elm)
- Les **Intents** représentent les intentions utilisateur (objets immuables)
- Le **Reducer** produit un nouveau `CalculatorState` immuable à chaque action
- La View re-rend l'UI complètement depuis un State complet
- Analogie React : `render(state)` est déclenché à chaque changement de Store

**Fichiers** : `CalculatorIntent.java`, `CalculatorState.java`, `CalculatorStore.java`, `CalculatorView.java`

---

### VIPER — View Interactor Presenter Entity Router

```
View ──►  Presenter ──► Interactor ──► Backend
  ▲            │              │
  └────────────┘              └──► Entity
       OutputPort
  Router (assemble tout)
```

- La **logique métier** est isolée dans l'**Interactor** (pas dans le Presenter)
- Le **Presenter** ne formate que les données pour la View (thin presenter)
- Le **Router** est la seule classe qui connaît toutes les couches et assure le wiring
- Application maximale du **Single Responsibility Principle**

**Fichiers** : `CalculatorEntity.java`, `CalculatorInteractorOutput.java`, `CalculatorInteractor.java`, `CalculatorViewContract.java`, `CalculatorView.java`, `CalculatorPresenter.java`, `CalculatorRouter.java`

---

## 📦 Prérequis

| Outil | Version minimale |
|-------|-----------------|
| Java (JDK) | 11 |
| Maven | 3.6+ |

Vérifier les versions installées :
```bash
java -version
mvn -version
```

---

## 🚀 Installation et lancement

### 1. Cloner le dépôt

```bash
git clone https://github.com/BenRep/tests-perfs-ads.git
cd /tests-perfs-ads/
```

### 2. Compiler le projet

Sur IntelliJ, le projet est reconnu en tant que projet Maven. Il suffit d'accepter la notification pour synchroniser le projet.

### 3. Choisir le pattern à lancer

Ouvrir le fichier `src/main/java/fr/memoire/Main.java` et modifier la ligne 36 :

```java
private static final String designPattern = "MVC";
```

Les valeurs acceptées sont :

| Valeur | Pattern lancé |
|--------|--------------|
| `"MVC"` | Model-View-Controller |
| `"MVP"` | Model-View-Presenter |
| `"MVVM"` | Model-View-ViewModel |
| `"MVI"` | Model-View-Intent |
| `"VIPER"` | View-Interactor-Presenter-Entity-Router |

### 4. Lancer l'application

```bash
mvn exec:java -Dexec.mainClass="fr.benrep.ads.Main"
```

Ou via votre IDE (IntelliJ, Eclipse, VS Code) : lancer directement la classe `Main.java`.

### 5. Utiliser l'interface

L'application Swing s'ouvre avec :

- **Opérande A** et **Opérande B** : saisir des entiers
- **Opération** : choisir `+`, `-` ou `*`
- **Calculer (backend)** : déclenche un appel au backend hexagonal et affiche le résultat
- **Reset (modèle)** : réinitialise le modèle local sans appel backend

---

## 📊 Mesure des performances

### Logs de performance

Chaque action déclenche un log au format :

```
HH:mm:ss.SSS [PATTERN][ACTION] Temps de réponse : X µs
```

Exemples :
```
14:23:01.042 [MVC][CALCULATE] Temps de réponse : 312 µs
14:23:01.043 [MVC][RESET]     Temps de réponse : 18 µs
```

### Fichier de résultats

Les logs de performance sont écrits dans :

```
results/performance-logs.txt
```

Ce fichier est en mode `append` : chaque session s'ajoute à la suite. Il est recommandé de le vider entre deux sessions de mesure pour garder des données propres.

### Protocole de test utilisé pour le mémoire

Pour reproduire les conditions du mémoire :

1. Lancer le pattern souhaité
2. Exécuter 12 fois la séquence : `3+8`, `9-2`, `6×7`, `2871+10557`, `26381-1398`, `1567×6481`, **Reset**
3. Relever les temps dans `results/performance-logs.txt` en enlevant les extrémums
4. Répéter pour chaque pattern

---

## 📁 Structure des fichiers

```
design-patterns-poc/
├── pom.xml
├── results/
│   └── performance-logs.txt          ← logs générés à l'exécution
└── src/
    └── main/
        ├── java/
        │   └── fr/memoire/
        │       ├── Main.java                          ← point d'entrée
        │       ├── backend/
        │       │   ├── adapter/
        │       │   │   └── CalculatorAdapter.java
        │       │   └── domain/
        │       │       ├── port/
        │       │       │   └── CalculatorUseCase.java
        │       │       └── service/
        │       │           └── CalculatorService.java
        │       ├── common/
        │       │   └── ViewConstants.java
        │       ├── mvc/
        │       │   ├── CalculatorModel.java
        │       │   ├── CalculatorView.java
        │       │   └── CalculatorController.java
        │       ├── mvp/
        │       │   ├── CalculatorModel.java
        │       │   ├── CalculatorViewContract.java
        │       │   ├── CalculatorView.java
        │       │   └── CalculatorPresenter.java
        │       ├── mvvm/
        │       │   ├── CalculatorModel.java
        │       │   ├── ObservableProperty.java
        │       │   ├── CalculatorViewModel.java
        │       │   └── CalculatorView.java
        │       ├── mvi/
        │       │   ├── CalculatorIntent.java
        │       │   ├── CalculatorState.java
        │       │   ├── CalculatorStore.java
        │       │   └── CalculatorView.java
        │       └── viper/
        │           ├── CalculatorEntity.java
        │           ├── CalculatorInteractorOutput.java
        │           ├── CalculatorInteractor.java
        │           ├── CalculatorViewContract.java
        │           ├── CalculatorView.java
        │           ├── CalculatorPresenter.java
        │           └── CalculatorRouter.java
        └── resources/
            └── logback.xml                            ← configuration des logs
```

---

## 🔬 Remarques méthodologiques

### Limites du POC

- Les calculs backend étant O(1), les temps mesurés reflètent principalement l'**overhead du pattern** (dispatch, binding, allocation) et non le temps métier.
- Les mesures en microsecondes restent sensibles au **JIT compiler** de la JVM : les premières exécutions sont plus lentes que les suivantes (warmup). Il est conseillé d'ignorer les 2-3 premières mesures de chaque session.
- Swing n'est pas un framework nativement conçu pour MVVM ou MVI : l'`ObservableProperty` et le `CalculatorStore` sont des implémentations maison qui simulent les mécanismes natifs de JavaFX ou Android.

### Choix de l'application de démonstration

Une calculatrice simple a été choisie délibérément pour minimiser la complexité fonctionnelle et maximiser la lisibilité des différences architecturales entre les patterns.

### Dépendances

| Dépendance | Version | Usage |
|-----------|---------|-------|
| SLF4J | 1.7.36 | API de logging |
| Logback | 1.2.11 | Implémentation des logs + écriture fichier |

---

## 📄 Licence

Ce projet est mis à disposition à des fins académiques. Aucune licence commerciale n'est associée.

---

*Projet généré avec l'assistance de [Claude](https://claude.ai) (Anthropic) — dans le cadre d'un mémoire ADS.*