import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-service-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './service-details.html',
  styleUrl: './service-details.scss'
})
export class ServiceDetails {
  features = [
    {
      icon: 'bi-mortarboard-fill',
      title: 'Formateurs Experts',
      description: 'Des professionnels certifiés avec une expérience terrain de plus de 10 ans dans leur domaine.',
      gradient: 'linear-gradient(135deg, #002b6c, #0056b3)'
    },
    {
      icon: 'bi-person-workspace',
      title: 'Formation Sur Mesure',
      description: 'Programmes adaptés à vos besoins spécifiques, votre secteur d\'activité et vos objectifs.',
      gradient: 'linear-gradient(135deg, #e60000, #cc0000)'
    },
    {
      icon: 'bi-laptop',
      title: 'Outils Modernes',
      description: 'Accès à des plateformes interactives, des supports numériques et des environnements de pratique.',
      gradient: 'linear-gradient(135deg, #0056b3, #007bff)'
    },
    {
      icon: 'bi-award-fill',
      title: 'Certification Reconnue',
      description: 'Obtenez des certifications reconnues par l\'industrie pour valoriser vos compétences.',
      gradient: 'linear-gradient(135deg, #002b6c, #003d8f)'
    },
    {
      icon: 'bi-people-fill',
      title: 'Suivi Personnalisé',
      description: 'Accompagnement individuel tout au long de la formation avec évaluation continue.',
      gradient: 'linear-gradient(135deg, #cc0000, #e60000)'
    },
    {
      icon: 'bi-shield-check',
      title: 'Garantie Qualité',
      description: 'Satisfaction garantie avec notre engagement qualité et notre processus d\'amélioration continue.',
      gradient: 'linear-gradient(135deg, #003d8f, #0056b3)'
    }
  ];

  processSteps = [
    {
      icon: 'bi-chat-square-text-fill',
      title: 'Analyse des besoins',
      description: 'Nous identifions vos objectifs, contraintes et le niveau de vos équipes.'
    },
    {
      icon: 'bi-pencil-square',
      title: 'Conception du programme',
      description: 'Création d\'un programme personnalisé avec des objectifs pédagogiques clairs.'
    },
    {
      icon: 'bi-play-circle-fill',
      title: 'Déploiement',
      description: 'Animation de la formation avec des méthodes interactives et pratiques.'
    },
    {
      icon: 'bi-graph-up-arrow',
      title: 'Évaluation & Suivi',
      description: 'Mesure des acquis, feedback et accompagnement post-formation.'
    }
  ];

  stats = [
    { icon: 'bi-people-fill', value: '500+', label: 'Professionnels formés' },
    { icon: 'bi-journal-check', value: '50+', label: 'Formations disponibles' },
    { icon: 'bi-star-fill', value: '98%', label: 'Taux de satisfaction' },
    { icon: 'bi-building', value: '120+', label: 'Entreprises clientes' }
  ];

  modalities = [
    {
      icon: 'bi-building',
      title: 'Présentiel',
      description: 'Formation en salle avec interaction directe et mise en pratique immédiate.',
      gradient: 'linear-gradient(135deg, #002b6c, #0056b3)',
      features: [
        'Interaction directe avec le formateur',
        'Exercices pratiques en groupe',
        'Networking avec les participants',
        'Support matériel fourni'
      ]
    },
    {
      icon: 'bi-camera-video-fill',
      title: 'À distance',
      description: 'Formation en ligne en temps réel avec les mêmes standards de qualité.',
      gradient: 'linear-gradient(135deg, #0056b3, #007bff)',
      features: [
        'Sessions en visioconférence',
        'Replay disponible',
        'Flexibilité géographique',
        'Plateforme interactive'
      ]
    },
    {
      icon: 'bi-arrows-angle-expand',
      title: 'Hybride',
      description: 'Le meilleur des deux mondes combinant présentiel et distanciel.',
      gradient: 'linear-gradient(135deg, #e60000, #cc0000)',
      features: [
        'Mix présentiel/distanciel',
        'Parcours flexible',
        'Suivi personnalisé',
        'Ressources en ligne 24/7'
      ]
    }
  ];
}
