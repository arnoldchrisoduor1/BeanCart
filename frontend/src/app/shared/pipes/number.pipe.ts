import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'number',
  standalone: true
})
export class NumberPipe implements PipeTransform {
  transform(value: number | undefined, digitsInfo?: string): string {
    if (value === undefined || value === null) return '';
    
    // Default format: '1.2-2' (min 1 digit before decimal, 2-2 digits after)
    const [minInteger, minFraction, maxFraction] = (digitsInfo || '1.2-2')
      .split('.')
      .flatMap(part => part.split('-'))
      .map(part => parseInt(part, 10));
    
    return value.toLocaleString(undefined, {
      minimumIntegerDigits: minInteger,
      minimumFractionDigits: minFraction,
      maximumFractionDigits: maxFraction
    });
  }
}