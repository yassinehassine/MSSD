import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface TranslationData {
  [key: string]: string;
}

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private currentLanguageSubject = new BehaviorSubject<string>('fr');
  public currentLanguage$ = this.currentLanguageSubject.asObservable();

  private translations: { [lang: string]: TranslationData } = {
    fr: {
      'nav.home': 'Accueil',
      'nav.about': 'À propos',
      'nav.annexes': 'Annexes',
      'nav.portfolio': 'Portfolio',
      'nav.blog': 'Blog',
      'nav.service-details': 'Détails du service',
      'nav.calendar': 'Calendrier',
      'nav.contact': 'Contact',
      'home.hero.title': 'Développez vos compétences avec MSSD',
      'home.hero.subtitle': 'Formations professionnelles, coaching personnalisé et solutions modernes pour accompagner la croissance de votre entreprise.',
      'home.hero.discover-formations': 'Découvrir nos formations',
      'home.hero.contact-us': 'Nous contacter',
      'home.about.title': 'À propos de MSSD',
      'home.about.description': 'Management Solutions & Sales Développement. Coaching de proximité, management par objectifs et processus, et accompagnement des transitions.',
      'home.about.learn-more': 'En savoir plus',
      'home.stats.happy-clients': 'Clients Satisfaits',
      'home.stats.projects': 'Projets',
      'home.stats.support-hours': 'Heures de Support',
      'home.stats.hard-workers': 'Travailleurs Acharnés',
      'home.explore.title': 'Explorez MSSD',
      'home.explore.subtitle': 'Accédez rapidement aux sections clés',
      'home.explore.formations': 'Formations',
      'home.explore.formations-desc': 'Découvrez nos programmes de formation',
      'home.explore.see-formations': 'Voir les formations',
      'home.explore.portfolio': 'Portfolio',
      'home.explore.portfolio-desc': 'Nos réalisations et projets',
      'home.explore.see-portfolio': 'Voir le portfolio',
      'home.explore.calendar': 'Calendrier',
      'home.explore.calendar-desc': 'Consultez nos événements',
      'home.explore.see-calendar': 'Voir le calendrier',
      'home.premium.title': 'Partenaires de Confiance',
      'home.premium.subtitle': 'Rejoignez des milliers d\'entreprises qui nous font confiance',
      'home.portfolio.title': 'Nos Réalisations',
      'home.portfolio.subtitle': 'Découvrez les entreprises qui nous ont fait confiance',
      'home.formations.title': 'Nos Formations Populaires',
      'home.formations.subtitle': 'Développez vos compétences avec nos formations les plus demandées',
      'home.events.title': 'Événements à venir',
      'home.events.subtitle': 'Participez à nos prochains événements et ateliers',
      'home.contact-cta.title': 'Prêt à accélérer votre performance commerciale ?',
      'home.contact-cta.subtitle': 'Contactez-nous pour une session de découverte gratuite.',
      'home.contact-cta.contact': 'Contact',
      'home.cta.title': 'Prêt à développer vos compétences ?',
      'home.cta.subtitle': 'Contactez-nous dès aujourd\'hui pour discuter de vos besoins en formation et obtenir un devis personnalisé.',
      'home.cta.button': 'Demander un devis',
      'portfolio.title': 'Portfolio',
      'portfolio.subtitle': 'Découvrez nos magnifiques réalisations',
      'portfolio.all-categories': 'Toutes les catégories',
      'portfolio.client': 'Client',
      'portfolio.date': 'Date',
      'portfolio.category': 'Catégorie',
      'portfolio.no-results': 'Aucun élément de portfolio trouvé pour la catégorie',
      'portfolio.try-different': 'Essayez de sélectionner une catégorie différente ou revenez plus tard.',
      'blog.title': 'Blog',
      'blog.subtitle': 'Découvrez nos derniers articles',
      'blog.read-more': 'Lire la suite',
      'blog.search': 'Rechercher...',
      'blog.all-articles': 'Tous les articles',
      'blog.no-results': 'Aucun article trouvé',
      'contact.title': 'Contact',
      'contact.get-in-touch': 'Contactez-nous',
      'contact.name': 'Nom',
      'contact.email': 'Email',
      'contact.subject': 'Sujet',
      'contact.message': 'Message',
      'contact.send': 'Envoyer',
      'about.title': 'À propos de MSSD',
      'about.subtitle': 'Management Solutions & Sales Développement',
      'about.mission': 'Notre Mission',
      'about.mission-desc': 'Accompagner les entreprises dans leur transformation et le développement des compétences de leurs équipes.',
      'about.vision': 'Notre Vision',
      'about.vision-desc': 'Devenir la référence en formation professionnelle et coaching de proximité.',
      'about.values': 'Nos Valeurs',
      'about.values-desc': 'Excellence, Innovation, Proximité et Engagement.',
      'calendar.title': 'Calendrier des Événements',
      'calendar.subtitle': 'Découvrez et participez à nos prochains événements et activités',
      'calendar.month': 'Mois',
      'calendar.list': 'Liste',
      'calendar.today': 'Aujourd\'hui',
      'calendar.search': 'Rechercher',
      'calendar.location': 'Lieu',
      'calendar.status': 'Statut',
      'calendar.available': 'Disponible',
      'calendar.full': 'Complet',
      'calendar.cancelled': 'Annulé',
      'calendar.completed': 'Terminé',
      'calendar.join': 'Participer',
      'calendar.reserve': 'Réserver votre place',
      'calendar.no-events': 'Aucun événement trouvé',
      'services.title': 'Nos Formations',
      'services.subtitle': 'Développez vos compétences avec nos formations professionnelles certifiantes',
      'services.book': 'Réserver',
      'services.reviews': 'Voir les avis',
      'services.custom-request': 'Demande personnalisée',
      'services.custom-subtitle': 'Vous ne trouvez pas ce qu\'il vous faut ? Décrivez votre besoin et nous reviendrons vers vous !',
      'footer.navigation': 'Navigation',
      'footer.services': 'Nos Services',
      'footer.contact': 'Contact',
      'footer.privacy': 'Politique de confidentialité',
      'footer.terms': 'Conditions d\'utilisation',
      'footer.legal': 'Mentions légales',
      'common.loading': 'Chargement...',
      'common.previous': 'Précédent',
      'common.next': 'Suivant',
      'common.pause': 'Pause',
      'common.play': 'Play',
      'common.automatic-scroll': 'Défilement automatique',
      'common.of': 'de'
    },
    en: {
      'nav.home': 'Home',
      'nav.about': 'About',
      'nav.annexes': 'Annexes',
      'nav.portfolio': 'Portfolio',
      'nav.blog': 'Blog',
      'nav.service-details': 'Service Details',
      'nav.calendar': 'Calendar',
      'nav.contact': 'Contact',
      'home.hero.title': 'Develop Your Skills with MSSD',
      'home.hero.subtitle': 'Professional training, personalized coaching and modern solutions to support your business growth.',
      'home.hero.discover-formations': 'Discover Our Training',
      'home.hero.contact-us': 'Contact Us',
      'home.about.title': 'About MSSD',
      'home.about.description': 'Management Solutions & Sales Development. Proximity coaching, management by objectives and processes, and transition support.',
      'home.about.learn-more': 'Learn More',
      'home.stats.happy-clients': 'Happy Clients',
      'home.stats.projects': 'Projects',
      'home.stats.support-hours': 'Hours Of Support',
      'home.stats.hard-workers': 'Hard Workers',
      'home.explore.title': 'Explore MSSD',
      'home.explore.subtitle': 'Quick access to key sections',
      'home.explore.formations': 'Training',
      'home.explore.formations-desc': 'Discover our training programs',
      'home.explore.see-formations': 'View Training',
      'home.explore.portfolio': 'Portfolio',
      'home.explore.portfolio-desc': 'Our achievements and projects',
      'home.explore.see-portfolio': 'View Portfolio',
      'home.explore.calendar': 'Calendar',
      'home.explore.calendar-desc': 'Check our events',
      'home.explore.see-calendar': 'View Calendar',
      'home.premium.title': 'Trusted by Industry Leaders',
      'home.premium.subtitle': 'Join thousands of companies that trust us with their growth',
      'home.portfolio.title': 'Our Work',
      'home.portfolio.subtitle': 'Discover the companies that trust us',
      'home.formations.title': 'Popular Training',
      'home.formations.subtitle': 'Grow your skills with our most in-demand courses',
      'home.events.title': 'Upcoming Events',
      'home.events.subtitle': 'Join our upcoming workshops and events',
      'home.contact-cta.title': 'Ready to accelerate your sales performance?',
      'home.contact-cta.subtitle': 'Contact us for a free discovery session.',
      'home.contact-cta.contact': 'Contact',
      'home.cta.title': 'Ready to grow your skills?',
      'home.cta.subtitle': 'Contact us today to discuss your training needs and get a personalized quote.',
      'home.cta.button': 'Request a quote',
      'portfolio.title': 'Portfolio',
      'portfolio.subtitle': 'Check out our beautiful work',
      'portfolio.all-categories': 'All Categories',
      'portfolio.client': 'Client',
      'portfolio.date': 'Date',
      'portfolio.category': 'Category',
      'portfolio.no-results': 'No portfolio items found for category',
      'portfolio.try-different': 'Try selecting a different category or check back later.',
      'blog.title': 'Blog',
      'blog.subtitle': 'Discover our latest articles',
      'blog.read-more': 'Read More',
      'blog.search': 'Search...',
      'blog.all-articles': 'All Articles',
      'blog.no-results': 'No articles found',
      'contact.title': 'Contact',
      'contact.get-in-touch': 'Get In Touch',
      'contact.name': 'Name',
      'contact.email': 'Email',
      'contact.subject': 'Subject',
      'contact.message': 'Message',
      'contact.send': 'Send',
      'about.title': 'About MSSD',
      'about.subtitle': 'Management Solutions & Sales Development',
      'about.mission': 'Our Mission',
      'about.mission-desc': 'Supporting companies in their transformation and developing their teams\' skills.',
      'about.vision': 'Our Vision',
      'about.vision-desc': 'Becoming the reference in professional training and proximity coaching.',
      'about.values': 'Our Values',
      'about.values-desc': 'Excellence, Innovation, Proximity and Commitment.',
      'calendar.title': 'Events Calendar',
      'calendar.subtitle': 'Discover and participate in our upcoming events and activities',
      'calendar.month': 'Month',
      'calendar.list': 'List',
      'calendar.today': 'Today',
      'calendar.search': 'Search',
      'calendar.location': 'Location',
      'calendar.status': 'Status',
      'calendar.available': 'Available',
      'calendar.full': 'Full',
      'calendar.cancelled': 'Cancelled',
      'calendar.completed': 'Completed',
      'calendar.join': 'Join',
      'calendar.reserve': 'Reserve your spot',
      'calendar.no-events': 'No events found',
      'services.title': 'Our Training Programs',
      'services.subtitle': 'Develop your skills with our certified professional training programs',
      'services.book': 'Book',
      'services.reviews': 'View Reviews',
      'services.custom-request': 'Custom Request',
      'services.custom-subtitle': 'Can\'t find what you need? Describe your requirements and we\'ll get back to you!',
      'footer.navigation': 'Navigation',
      'footer.services': 'Our Services',
      'footer.contact': 'Contact',
      'footer.privacy': 'Privacy Policy',
      'footer.terms': 'Terms & Conditions',
      'footer.legal': 'Legal Notice',
      'common.loading': 'Loading...',
      'common.previous': 'Previous',
      'common.next': 'Next',
      'common.pause': 'Pause',
      'common.play': 'Play',
      'common.automatic-scroll': 'Automatic Scroll',
      'common.of': 'of'
    }
  };

  constructor() {
    const savedLang = localStorage.getItem('mssd-language');
    if (savedLang && (savedLang === 'fr' || savedLang === 'en')) {
      this.currentLanguageSubject.next(savedLang);
    }
  }

  getCurrentLanguage(): string {
    return this.currentLanguageSubject.value;
  }

  switchLanguage(lang: 'fr' | 'en'): void {
    this.currentLanguageSubject.next(lang);
    localStorage.setItem('mssd-language', lang);
  }

  translate(key: string): string {
    const currentLang = this.getCurrentLanguage();
    const found = this.translations[currentLang]?.[key];
    if (found && found.trim() !== '') {
      return found;
    }
    if (key && key.includes('.')) {
      return key.split('.').join(' ').replace(/\s+/g, ' ').trim();
    }
    return key;
  }

  t(key: string): string {
    return this.translate(key);
  }
}
