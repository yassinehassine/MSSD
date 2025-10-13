import { Component } from '@angular/core';
import { ContactService, ContactDto } from '../../services/contact.service';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './contact.html',
  styleUrl: './contact.scss'
})
export class Contact {
  isSubmitting = false;
  submitSuccess = false;
  submitError = '';

  constructor(private contactService: ContactService) {}

  onSubmit(event: Event) {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    this.isSubmitting = true;
    this.submitError = '';
    this.submitSuccess = false;

    const formData = new FormData(form);
    const contactDto: ContactDto = {
      fullName: formData.get('name') as string,
      email: formData.get('email') as string,
      phone: formData.get('phone') as string,
      subject: formData.get('subject') as string,
      message: formData.get('message') as string
    };

    this.contactService.submitContact(contactDto).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.submitSuccess = true;
        form.reset();
      },
      error: () => {
        this.isSubmitting = false;
        this.submitError = 'Failed to send message. Please try again.';
      }
    });
  }
}
