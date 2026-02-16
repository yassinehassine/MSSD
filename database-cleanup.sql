-- Script de nettoyage de la base de données
-- Suppression des tables inutiles et conservation uniquement d'annex_requests pour les demandes de formations

-- Désactiver les contraintes de clés étrangères temporairement
SET FOREIGN_KEY_CHECKS = 0;

-- Supprimer les tables inutiles liées aux réservations/bookings
DROP TABLE IF EXISTS custom_requests;
DROP TABLE IF EXISTS formation_bookings;
DROP TABLE IF EXISTS reservations;

-- Réactiver les contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;

-- Vérifier que la table annex_requests existe et a la bonne structure
-- Cette table centralisera toutes les demandes de formation
DESCRIBE annex_requests;

-- Optionnel : Afficher le contenu actuel de la table
SELECT COUNT(*) as total_requests FROM annex_requests;
SELECT status, COUNT(*) as count 
FROM annex_requests 
GROUP BY status;