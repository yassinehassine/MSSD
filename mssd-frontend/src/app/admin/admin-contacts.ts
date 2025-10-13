import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactService, ContactDto } from '../services/contact.service';

@Component({
  selector: 'app-admin-contacts',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-contacts.html',
  styleUrl: './admin-contacts.scss'
})
export class AdminContacts implements OnInit {
  contacts: ContactDto[] = [];
  loading = true;
  error = '';

  constructor(private contactService: ContactService) {}

  ngOnInit() {
    this.contactService.getAllContacts().subscribe({
      next: (data) => {
        this.contacts = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load contacts.';
        this.loading = false;
      }
    });
  }
} 