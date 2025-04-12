import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@Component({
    selector: 'hero-widget',
    imports: [ButtonModule, RippleModule],
    template: `
        <div id="hero" class="flex flex-col pt-6 px-6 lg:px-20 overflow-hidden" style="background: linear-gradient(180deg, rgba(0,0,0,0.9), rgba(0,0,0,0.6)); clip-path: ellipse(150% 87% at 93% 13%);">
            <div class="mx-6 md:mx-20 mt-0 md:mt-6">
                <h1 class="text-6xl font-bold text-white leading-tight">
                    <span class="font-light block">Welcome to</span>
                    Micromall — Your Smart Shopping Hub
                </h1>
                <p class="font-normal text-2xl leading-normal md:mt-4 text-gray-300">
                    Discover thousands of products, unbeatable deals, and a seamless shopping experience — all in one place. Micromall makes it easy to shop smart, save more, and get what you need fast.
                </p>
                <button pButton pRipple [rounded]="true" type="button" label="Start Shopping" class="!text-xl mt-8 !px-4"></button>
            </div>
        </div>
    `
})
export class HeroWidget {}
