import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ContactService, ContactDto } from '../../services/contact.service';
import { ToastService } from '../../services/toast.service';
import { TranslationService } from '../../services/translation.service';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contact.html',
  styleUrl: './contact.scss'
})
export class Contact {
  isSubmitting = false;

  contactModel: ContactDto = {
    fullName: '',
    email: '',
    phone: '',
    subject: '',
    message: ''
  };

  constructor(
    private contactService: ContactService,
    private toast: ToastService,
    public translationService: TranslationService
  ) {}

  t(key: string): string {
    return this.translationService.translate(key);
  }

  onSubmit(form: NgForm) {
    if (form.invalid) {
      Object.values(form.controls).forEach(c => c.markAsTouched());
      this.toast.warning('Veuillez remplir tous les champs requis.');
      return;
    }

    this.isSubmitting = true;

    this.contactService.submitContact(this.contactModel).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.toast.success('Votre message a été envoyé avec succès !');
        form.resetForm();
        this.contactModel = { fullName: '', email: '', phone: '', subject: '', message: '' };
      },
      error: () => {
        this.isSubmitting = false;
        this.toast.error('Échec de l\'envoi du message. Veuillez réessayer.');
      }
    });
  }
}
