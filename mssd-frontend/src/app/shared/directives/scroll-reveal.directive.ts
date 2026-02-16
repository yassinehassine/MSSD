import { Directive, ElementRef, OnInit, OnDestroy, Input } from '@angular/core';

@Directive({
  selector: '[appScrollReveal]',
  standalone: true
})
export class ScrollRevealDirective implements OnInit, OnDestroy {
  @Input() revealDelay = 0;
  @Input() revealDirection: 'up' | 'down' | 'left' | 'right' = 'up';

  private observer!: IntersectionObserver;

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    const element = this.el.nativeElement as HTMLElement;
    element.style.opacity = '0';
    element.style.transition = `opacity 0.6s ease ${this.revealDelay}ms, transform 0.6s ease ${this.revealDelay}ms`;

    switch (this.revealDirection) {
      case 'up': element.style.transform = 'translateY(30px)'; break;
      case 'down': element.style.transform = 'translateY(-30px)'; break;
      case 'left': element.style.transform = 'translateX(30px)'; break;
      case 'right': element.style.transform = 'translateX(-30px)'; break;
    }

    this.observer = new IntersectionObserver(
      (entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            element.style.opacity = '1';
            element.style.transform = 'translate(0)';
            this.observer.unobserve(element);
          }
        });
      },
      { threshold: 0.1, rootMargin: '0px 0px -50px 0px' }
    );

    this.observer.observe(element);
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }
}
