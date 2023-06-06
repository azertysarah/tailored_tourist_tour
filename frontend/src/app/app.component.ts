import { Component, OnChanges, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { T3Service } from './t3.service';
import { MapComponent } from './map/map.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnChanges {
  @ViewChild(MapComponent) mapComponent!: MapComponent;
  
  title = 'frontend';
  errorMessage: string = '';
  isLoading: boolean = false;

  // periods: any;
  // communes: any;
  results: any[] = [];
  updatedMonuments: any[] = [];

  researchForm = new FormGroup({
    monumentName: new FormControl('', Validators.required),
    region: new FormControl(''),
    period: new FormControl(''),
    time: new FormControl(null, Validators.required),
    needRealTime: new FormControl(false, Validators.required)
  })

  constructor(
    private t3service: T3Service
  ) {}

  ngOnChanges(): void {
    console.warn(this.results)
    // this.getAllPeriods();
    // this.getAllCommunes();
  }

  // getAllPeriods() {
  //   this.t3service.getAllPeriods().subscribe({
  //     next: (res: any) => this.periods = res,
  //     error: () => console.warn('An error occured')
  //   });
  // }

  // getAllCommunes(){
  //   this.t3service.getAllCommunes().subscribe({
  //     next: (res: any) => this.communes = res,
  //     error: () => console.warn('An error occured')
  //   });
  // }

  onSubmit() {
    console.log(this.researchForm.value);
    this.isLoading = true;
    this.t3service.postSearch(this.researchForm.value).subscribe({
      next: (res: any) => {
        for(let monument of res) {
          this.results.push({
            name: monument.fields.name,
            coordinates: monument.fields.coordinates,
            distance: monument.distance
          })
        }
        this.updatedMonuments = this.results; 
      },
      error: () => {
        this.isLoading = false;
        console.warn("An error occured in postSearch() method"),
        this.errorMessage = "Result unavailable"
      },
      complete: () => this.isLoading = false
    });
  }
  updateMonuments() {
    this.mapComponent.monuments = this.updatedMonuments;
  }
}
