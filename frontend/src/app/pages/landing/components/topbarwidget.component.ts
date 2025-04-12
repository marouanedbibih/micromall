import { Component } from '@angular/core';
import { StyleClassModule } from 'primeng/styleclass';
import { Router, RouterModule } from '@angular/router';
import { RippleModule } from 'primeng/ripple';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'topbar-widget',
    imports: [RouterModule, StyleClassModule, ButtonModule, RippleModule],
    template: `
    <a class="flex items-center" href="#">
            <span class="text-surface-900 dark:text-surface-0 font-medium text-2xl leading-normal mr-20">MICROMALL</span>
    </a>

        <a pButton [text]="true" severity="secondary" [rounded]="true" pRipple class="lg:!hidden" pStyleClass="@next" enterClass="hidden" leaveToClass="hidden" [hideOnOutsideClick]="true">
            <i class="pi pi-bars !text-2xl"></i>
        </a>

        <div class="items-center  bg-surface-0 dark:bg-surface-900 grow justify-end hidden lg:flex absolute lg:static w-full left-0 top-full px-12 lg:px-0 z-20 rounded-border">
            <ul class="list-none p-0 m-0 flex lg:items-center select-none flex-col lg:flex-row cursor-pointer gap-8">
                <li>
                    <a (click)="router.navigate(['/landing'], { fragment: 'home' })" pRipple class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                        <i class="pi pi-home mr-2" style="font-size: 1.5rem"></i>
                </a>
                </li>
                <li>
                    <a (click)="router.navigate(['/products'])" pRipple class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                    <i class="pi pi-shopping-bag mr-2" style="font-size: 1.5rem"></i>
                    </a>
                </li>
                <li>
                    <a (click)="router.navigate(['/login'])" pRipple class="px-0 py-4 text-surface-900 dark:text-surface-0 font-medium text-xl">
                    <i class="pi pi-sign-in mr-2" style="font-size: 1.5rem"></i>
                    </a>
                </li>
            </ul>
        </div> `
})
export class TopbarWidget {
    constructor(public router: Router) {}
}
