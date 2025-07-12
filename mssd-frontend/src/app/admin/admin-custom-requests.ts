import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomRequestService, CustomRequestResponse, CustomRequestUpdate } from '../services/custom-request.service';

@Component({
  selector: 'app-admin-custom-requests',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-custom-requests.html',
  styleUrl: './admin-contacts.scss'
})
export class AdminCustomRequests implements OnInit {
  requests: CustomRequestResponse[] = [];
  isLoading = true;
  error = '';
  editingId: number | null = null;
  editNotes: string = '';
  editStatus: string = '';
  updateError: string = '';
  updateSuccess: string = '';

  constructor(private customRequestService: CustomRequestService) {}

  ngOnInit() {
    this.loadRequests();
  }

  loadRequests() {
    this.isLoading = true;
    this.customRequestService.getAllCustomRequests().subscribe({
      next: (data) => {
        this.requests = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Failed to load custom requests.';
        this.isLoading = false;
      }
    });
  }

  startEdit(request: CustomRequestResponse) {
    this.editingId = request.id;
    this.editNotes = request.adminNotes || '';
    this.editStatus = request.status;
    this.updateError = '';
    this.updateSuccess = '';
  }

  saveEdit(request: CustomRequestResponse) {
    const update: CustomRequestUpdate = {
      status: this.editStatus,
      adminNotes: this.editNotes
    };
    this.customRequestService.updateCustomRequest(request.id, update).subscribe({
      next: (updated) => {
        request.status = updated.status;
        request.adminNotes = updated.adminNotes;
        this.editingId = null;
        this.updateSuccess = 'Updated successfully!';
      },
      error: () => {
        this.updateError = 'Failed to update.';
      }
    });
  }

  cancelEdit() {
    this.editingId = null;
    this.editNotes = '';
    this.editStatus = '';
    this.updateError = '';
    this.updateSuccess = '';
  }
} 