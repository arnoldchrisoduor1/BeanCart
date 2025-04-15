import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WishesPageComponent } from './wishes-page.component';

describe('WishesPageComponent', () => {
  let component: WishesPageComponent;
  let fixture: ComponentFixture<WishesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WishesPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WishesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
