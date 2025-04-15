import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShopProfileComponent } from './shop-profile.component';

describe('ShopProfileComponent', () => {
  let component: ShopProfileComponent;
  let fixture: ComponentFixture<ShopProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShopProfileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShopProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
