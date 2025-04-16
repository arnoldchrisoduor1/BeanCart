import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectionTabsComponent } from './selection-tabs.component';

describe('SelectionTabsComponent', () => {
  let component: SelectionTabsComponent;
  let fixture: ComponentFixture<SelectionTabsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectionTabsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectionTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
