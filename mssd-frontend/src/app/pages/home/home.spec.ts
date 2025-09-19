import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { Home } from './home';
import { PortfolioService } from '../../services/portfolio.service';
import { CalendarService } from '../../services/calendar.service';

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;
  let portfolioService: jasmine.SpyObj<PortfolioService>;
  let calendarService: jasmine.SpyObj<CalendarService>;

  beforeEach(async () => {
    const portfolioSpy = jasmine.createSpyObj('PortfolioService', ['getActivePortfolios']);
    const calendarSpy = jasmine.createSpyObj('CalendarService', ['getNextTwoWeeksEvents']);

    await TestBed.configureTestingModule({
      imports: [Home, HttpClientTestingModule],
      providers: [
        { provide: PortfolioService, useValue: portfolioSpy },
        { provide: CalendarService, useValue: calendarSpy }
      ]
    })
    .compileComponents();

    portfolioService = TestBed.inject(PortfolioService) as jasmine.SpyObj<PortfolioService>;
    calendarService = TestBed.inject(CalendarService) as jasmine.SpyObj<CalendarService>;
    
    // Setup default return values
    portfolioService.getActivePortfolios.and.returnValue(of([]));
    calendarService.getNextTwoWeeksEvents.and.returnValue(of([]));

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load upcoming events from next two weeks on init', () => {
    expect(calendarService.getNextTwoWeeksEvents).toHaveBeenCalled();
  });

  it('should clean up subscription on destroy', () => {
    spyOn(component['refreshSubscription']!, 'unsubscribe');
    component.ngOnDestroy();
    expect(component['refreshSubscription']!.unsubscribe).toHaveBeenCalled();
  });
});
