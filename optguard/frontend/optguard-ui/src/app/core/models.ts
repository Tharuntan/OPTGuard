export interface User {
  id: number;
  email: string;
  fullName: string;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface MessageResponse {
  message: string;
}

export interface StudentProfile {
  id?: number;
  schoolName?: string;
  dsoName?: string;
  dsoEmail?: string;
  sevisId?: string;
  visaStatus?: string;
  passportExpiryDate?: string;
}

export interface OptRecord {
  id?: number;
  optStartDate?: string;
  optEndDate?: string;
  stemStartDate?: string;
  stemEndDate?: string;
  eadStartDate?: string;
  eadEndDate?: string;
  unemploymentDaysUsed?: number;
  unemploymentDaysRemaining?: number;
  status?: string;
}

export interface Employer {
  id?: number;
  employerName: string;
  ein?: string;
  eVerifyNumber?: string;
  jobTitle?: string;
  employmentStartDate?: string;
  employmentEndDate?: string;
  worksiteAddress?: string;
  supervisorName?: string;
  supervisorEmail?: string;
  current: boolean;
}

export interface Deadline {
  id: number;
  deadlineType: string;
  title: string;
  description: string;
  dueDate?: string;
  status: 'PENDING' | 'COMPLETED';
  priority: 'SAFE' | 'WARNING' | 'URGENT';
  generated: boolean;
}

export interface EmailTemplate {
  id: number;
  templateType: string;
  title: string;
  subject: string;
  body: string;
}

export interface DashboardSummary {
  currentStatus: string;
  optEndDate?: string;
  stemEndDate?: string;
  unemploymentDaysUsed: number;
  unemploymentDaysRemaining: number;
  upcomingDeadlines: Deadline[];
  urgentDeadlines: Deadline[];
  warningMessage: string;
}
