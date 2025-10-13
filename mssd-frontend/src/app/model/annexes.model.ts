export interface Theme {
  id: number;
  name: string;
  slug: string;
  description: string;
  iconUrl?: string;
  active: boolean;
  formations?: FormationSummary[];
}

export interface FormationSummary {
  id: number;
  title: string;
  slug: string;
  description: string;
  price: number;
  duration: string;
  imageUrl?: string;
  level: 'BEGINNER' | 'INTERMEDIATE' | 'EXPERT';
  themeId?: number;
  themeName?: string;
}

export interface AnnexRequest {
  companyName: string;
  email: string;
  phone?: string;
  formationId?: number;
  isCustom: boolean;
  customDescription?: string;
  numParticipants: number;
  modality: 'IN_PERSON' | 'REMOTE' | 'HYBRID';
  preferredDate: string;
  notes?: string;
}

export interface AnnexRequestResponse extends AnnexRequest {
  id: number;
  formationTitle?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'IN_PROGRESS' | 'COMPLETED';
  createdAt: string;
  updatedAt: string;
}

export interface ModalityOption {
  value: 'IN_PERSON' | 'REMOTE' | 'HYBRID';
  label: string;
}