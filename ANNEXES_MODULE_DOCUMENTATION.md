# Annexes Module Documentation

## Overview
The Annexes module provides a hierarchical training catalog system where:
- **Themes** group related formations (e.g., "Soft Skills", "Marketing Digital")
- **Formations** are individual training courses under themes
- **Annex Requests** allow users to request existing or custom training

## Frontend Navigation

### Public Routes
- `/annexes` - Main themes grid view
- `/annexes/:slug` - Theme detail with formations list
- `/annexes/request` - Training request form (supports `?theme=slug` query param)

### Admin Routes  
- `/admin/themes` - Theme management (CRUD operations)

## API Endpoints

### Theme Endpoints

#### Public Endpoints
```
GET /api/themes
- Returns: List of active themes (without formations)
- Use: For dropdown selections

GET /api/themes/with-formations  
- Returns: Active themes with their published formations
- Use: Main annexes page display

GET /api/themes/{slug}/formations
- Returns: Single theme with its formations by slug
- Use: Theme detail page
```

#### Admin Endpoints
```
GET /api/themes/admin
- Returns: All themes (including inactive) with formation counts
- Use: Admin theme management list

POST /api/themes
- Body: ThemeCreateUpdateDto
- Returns: Created theme
- Use: Create new theme

PUT /api/themes/{id}
- Body: ThemeCreateUpdateDto  
- Returns: Updated theme
- Use: Update existing theme

DELETE /api/themes/{id}
- Returns: 204 No Content
- Use: Delete theme (only if no formations exist)
- Error: 400 if theme has formations
```

### Formation Endpoints
```
GET /api/formations
- Returns: List of all published formations (flat list)
- Use: Request form dropdown
```

### Annex Request Endpoints
```
POST /api/annex-requests
- Body: AnnexRequestDto
- Returns: Created request
- Use: Submit training request

GET /api/annex-requests (Admin)
- Returns: List of all requests
- Use: Admin request management
```

## Data Transfer Objects

### ThemeCreateUpdateDto
```typescript
{
  name: string;           // Display name
  slug: string;           // URL slug (a-z, 0-9, hyphens only)
  description?: string;   // Optional description
  iconUrl?: string;       // Optional icon image URL
  active: boolean;        // Visibility on public site
}
```

### ThemeDto (Response)
```typescript
{
  id: number;
  name: string;
  slug: string;
  description?: string;
  iconUrl?: string;
  active: boolean;
  formations?: FormationSummaryDto[]; // Included in with-formations endpoints
}
```

### AnnexRequestDto
```typescript
{
  companyName: string;
  email: string;
  phone?: string;
  isCustom: boolean;
  formationId?: number;        // Required if isCustom = false
  customDescription?: string;  // Required if isCustom = true
  modality: string;           // 'IN_PERSON', 'REMOTE', 'HYBRID'
  urgency: string;            // 'LOW', 'MEDIUM', 'HIGH'
  budget?: number;
  expectedDate?: Date;
  additionalNotes?: string;
}
```

## Frontend Components

### AnnexesComponent (`/annexes`)
- Displays theme cards in responsive grid
- Each card links to `/annexes/:slug`
- Shows formation counts and stats per theme

### AnnexesThemeDetail (`/annexes/:slug`)
- Shows theme details and associated formations
- "Request training" button navigates to form with `?theme=slug`

### AnnexesRequest (`/annexes/request`)
- Dynamic form with existing/custom training toggle
- Query param `?theme=slug` pre-selects formations from that theme
- Supports all modality and urgency options

### AdminThemes (`/admin/themes`)
- Full CRUD interface for theme management
- Inline editing with validation
- Delete protection for themes with formations
- Auto-slug generation from name

## Database Schema

### Theme Entity
```sql
CREATE TABLE theme (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    icon_url VARCHAR(500),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Formation Updates
- Added `theme_id` foreign key to existing Formation table
- Maintains backward compatibility with existing `category` field

### Seed Data
- Themes: "Soft Skills", "Marketing Digital", "Qualité & Certification" 
- Formations automatically assigned to themes based on category mapping
- Sample data includes calendar events, highlights, and company info

## Error Handling

### Common Error Responses
- `404 NOT_FOUND` - Theme/formation not found
- `400 BAD_REQUEST` - Validation errors, cannot delete theme with formations
- `409 CONFLICT` - Slug already exists

### Frontend Error Display
- Toast notifications for CRUD operations
- Inline form validation with specific error messages
- Loading states and graceful error fallbacks

## Performance Considerations

- Theme listings use `@Transactional(readOnly = true)` 
- Lazy loading of formation relationships where appropriate
- Frontend caching of formation lists for dropdowns
- Responsive design with mobile-first approach

## Future Enhancements

- Theme ordering/sorting options
- Theme image uploads with file management
- Formation filtering by theme in admin
- Bulk operations for theme management
- Theme analytics and usage statistics