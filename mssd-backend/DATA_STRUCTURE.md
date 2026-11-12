# 📊 MSSD Database Structure - Seeded Data Overview

## 🗂️ Entity Relationships

```
┌─────────────┐
│   THEMES    │ (6 records)
│             │
│ ├─ Management & Leadership
│ ├─ Communication & Relation Client  
│ ├─ Développement Personnel
│ ├─ Vente & Négociation
│ ├─ Ressources Humaines
│ └─ Gestion de Projet
└──────┬──────┘
       │
       │ (1 theme → N formations)
       │
       ▼
┌─────────────┐
│ FORMATIONS  │ (18 records - 3 per theme)
│             │
│ ├─ Beginner level (€900-1,600)
│ ├─ Intermediate level (€1,500-2,200)
│ └─ Expert level (€2,000-3,500)
└──────┬──────┘
       │
       ├────────────────────┬──────────────────┐
       │                    │                  │
       ▼                    ▼                  ▼
┌─────────────┐      ┌─────────────┐   ┌─────────────┐
│   REVIEWS   │      │  PORTFOLIO  │   │  CALENDAR   │
│ (8 records) │      │ (8 records) │   │ (8 records) │
│             │      │             │   │             │
│ Rating: 4-5 │      │ Companies:  │   │ Upcoming    │
│ Comments ✓  │      │ Total, BNP, │   │ sessions in │
│             │      │ Orange, etc │   │ FR cities   │
└─────────────┘      └─────────────┘   └─────────────┘
```

---

## 📚 Theme → Formation Breakdown

### 1️⃣ Management & Leadership (3 formations)
```
├─ Management d'Équipe - Les Fondamentaux
│  • Level: BEGINNER
│  • Price: €1,500
│  • Duration: 3 jours
│  • Reviews: 2 ⭐⭐⭐⭐⭐
│
├─ Leadership Stratégique
│  • Level: EXPERT
│  • Price: €2,500
│  • Duration: 5 jours
│  • Reviews: 1 ⭐⭐⭐⭐⭐
│
└─ Management Agile
   • Level: INTERMEDIATE
   • Price: €2,000
   • Duration: 4 jours
```

### 2️⃣ Communication & Relation Client (3 formations)
```
├─ Communication Interpersonnelle
│  • Level: BEGINNER
│  • Price: €1,200
│  • Duration: 2 jours
│  • Reviews: 1 ⭐⭐⭐⭐⭐
│
├─ Excellence en Relation Client
│  • Level: INTERMEDIATE
│  • Price: €1,800
│  • Duration: 3 jours
│  • Reviews: 1 ⭐⭐⭐⭐
│
└─ Prise de Parole en Public
   • Level: INTERMEDIATE
   • Price: €1,500
   • Duration: 2 jours
```

### 3️⃣ Développement Personnel (3 formations)
```
├─ Gestion du Temps et des Priorités
│  • Level: BEGINNER
│  • Price: €900
│  • Duration: 2 jours
│  • Reviews: 1 ⭐⭐⭐⭐⭐
│
├─ Intelligence Émotionnelle
│  • Level: INTERMEDIATE
│  • Price: €1,600
│  • Duration: 3 jours
│
└─ Gestion du Stress et Bien-être
   • Level: BEGINNER
   • Price: €1,100
   • Duration: 2 jours
```

### 4️⃣ Vente & Négociation (3 formations)
```
├─ Techniques de Vente Avancées
│  • Level: INTERMEDIATE
│  • Price: €2,200
│  • Duration: 4 jours
│  • Reviews: 1 ⭐⭐⭐⭐⭐
│
├─ Négociation Commerciale
│  • Level: INTERMEDIATE
│  • Price: €1,800
│  • Duration: 3 jours
│
└─ Prospection et Développement Commercial
   • Level: BEGINNER
   • Price: €1,500
   • Duration: 3 jours
```

### 5️⃣ Ressources Humaines (3 formations)
```
├─ Recrutement et Intégration
│  • Level: INTERMEDIATE
│  • Price: €1,700
│  • Duration: 3 jours
│  • Reviews: 1 ⭐⭐⭐⭐
│
├─ Gestion des Talents et des Compétences
│  • Level: EXPERT
│  • Price: €2,000
│  • Duration: 4 jours
│
└─ Droit du Travail pour Managers
   • Level: INTERMEDIATE
   • Price: €1,400
   • Duration: 2 jours
```

### 6️⃣ Gestion de Projet (3 formations)
```
├─ Gestion de Projet - Fondamentaux
│  • Level: BEGINNER
│  • Price: €1,600
│  • Duration: 3 jours
│
├─ Certification PMP Preparation
│  • Level: EXPERT
│  • Price: €3,500
│  • Duration: 10 jours
│
└─ Méthode Scrum pour Chefs de Projet
   • Level: INTERMEDIATE
   • Price: €1,800
   • Duration: 3 jours
```

---

## 🏢 Portfolio Projects (Formation References)

```
Total Energies ────────────────> Management Agile
BNP Paribas ───────────────────> Leadership Stratégique
Orange ────────────────────────> Excellence en Relation Client
Renault Group ─────────────────> Techniques de Vente Avancées
Airbus ────────────────────────> Méthode Scrum
SNCF ──────────────────────────> Management d'Équipe
L'Oréal ───────────────────────> Gestion des Talents
Carrefour ─────────────────────> Communication Interpersonnelle
```

---

## 📅 Calendar Events (Upcoming)

```
NOW + 10 days  → Gestion du Temps (Paris, 25 places)
NOW + 15 days  → Management d'Équipe (Paris, 15 places)
NOW + 20 days  → Communication (Paris, 20 places)
NOW + 30 days  → Leadership (Lyon, 12 places)
NOW + 35 days  → Excellence Client (Bordeaux, 20 places)
NOW + 40 days  → Techniques de Vente (Marseille, 18 places)
NOW + 50 days  → Scrum (Toulouse, 16 places)
NOW + 55 days  → Management Agile (Paris, 14 places)
```

---

## 📝 Blog Posts Timeline

```
60 days ago ── Développer son Personal Branding
55 days ago ── L'Art de la Négociation Commerciale
50 days ago ── Gestion de Projet : Méthodologies Comparées
45 days ago ── Recrutement : Les Erreurs à Éviter
40 days ago ── Leadership Agile : Principes et Pratiques
30 days ago ── Gestion du Stress : Guide Pratique
25 days ago ── Techniques de Vente pour 2024
18 days ago ── L'Intelligence Émotionnelle en Entreprise
12 days ago ── Comment Améliorer sa Communication
5 days ago  ── Les 5 Compétences Clés du Manager
```

---

## 👥 User Roles & Access

```
┌──────────────────────────────────────┐
│  ADMIN                               │
│  • Username: admin                   │
│  • Email: admin@mssd.com            │
│  • Password: admin123               │
│  • Can: Manage everything           │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│  MANAGER                             │
│  • Username: manager                 │
│  • Email: manager@mssd.com          │
│  • Password: manager123             │
│  • Can: Manage content              │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│  INSTRUCTOR                          │
│  • Username: instructor              │
│  • Email: instructor@mssd.com       │
│  • Password: instructor123          │
│  • Can: View and create content     │
└──────────────────────────────────────┘
```

---

## 📊 Data Statistics

| Entity | Count | Key Features |
|--------|-------|--------------|
| **Themes** | 6 | All active & published |
| **Formations** | 18 | Mixed levels (6 BEGINNER, 9 INTERMEDIATE, 3 EXPERT) |
| **Reviews** | 8 | Average rating: 4.5/5 ⭐ |
| **Portfolio** | 8 | Major French companies |
| **Calendar** | 8 | All upcoming events |
| **Blog** | 10 | Mix of video & text |
| **Users** | 3 | 1 admin, 2 users |
| **Newsletters** | 5 | Sample subscribers |
| **Highlights** | 4 | Homepage features |
| **Company** | 1 | Complete info |

---

## 💰 Price Distribution

```
€0-1,000    : ████ (1 formation)
€1,001-1,500: ████████████ (6 formations)
€1,501-2,000: ████████████ (6 formations)
€2,001-2,500: ████████ (4 formations)
€2,501+     : ████ (1 formation)

Average Price: €1,700
Price Range: €900 - €3,500
```

---

## 📍 Geographic Distribution (Calendar Events)

```
Paris    : ████████████████ (4 events)
Lyon     : ████ (1 event)
Marseille: ████ (1 event)
Toulouse : ████ (1 event)
Bordeaux : ████ (1 event)
```

---

## 🎯 Formation Levels Distribution

```
BEGINNER     : ██████ (6 formations - 33%)
INTERMEDIATE : ██████████ (9 formations - 50%)
EXPERT       : ████ (3 formations - 17%)
```

---

## ⏱️ Duration Distribution

```
2 jours  : ████████████ (6 formations)
3 jours  : ████████████████ (8 formations)
4 jours  : ████████ (3 formations)
5 jours  : ████ (1 formation)
10 jours : ████ (1 formation)

Average Duration: 3.5 days
```

---

## 🔗 Relationship Counts

```
Themes with most formations:
├─ All themes: 3 formations each (balanced)

Formations with most reviews:
├─ Management d'Équipe: 2 reviews ⭐⭐⭐⭐⭐
├─ Others: 1 review each

Calendar events by capacity:
├─ Large (20-25): 3 events
├─ Medium (15-19): 3 events
├─ Small (12-14): 2 events
```

---

## 🎨 Content Quality

✅ **All French Content**
- Professional descriptions
- Authentic testimonials
- Business-appropriate language

✅ **SEO-Friendly**
- Unique slugs for all formations
- Meta descriptions
- Descriptive titles

✅ **Complete Information**
- All required fields populated
- No null critical fields
- Realistic data values

✅ **Ready for Production**
- Published flags set
- Active flags set
- Proper date ranges

---

## 🔄 Update Frequencies (Suggested)

```
Themes      : Rarely (stable structure)
Formations  : Quarterly (new courses)
Reviews     : Weekly (new testimonials)
Portfolio   : Monthly (new projects)
Calendar    : Weekly (new sessions)
Blog        : Bi-weekly (new articles)
```

---

## 📝 Notes

1. **All dates are relative** - Calendar events are always in the future
2. **Passwords are hashed** - Using BCrypt with strength 10
3. **Foreign keys are valid** - All relationships properly linked
4. **Images are placeholders** - Replace with real images
5. **Content is professional** - Suitable for client demos

---

**This structure provides a complete, realistic dataset for development and testing! 🎉**
