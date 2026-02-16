/**
* Template Name: FlexStart
* Template URL: https://bootstrapmade.com/flexstart-bootstrap-startup-template/
* Updated: Nov 01 2024 with Bootstrap v5.3.3
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/

(function() {
  "use strict";

  /**
   * Preloader
   */
  const preloader = document.querySelector('#preloader');
  if (preloader) {
    window.addEventListener('load', () => {
      preloader.remove();
    });
  }

  /**
   * Sticky header on scroll
   */
  const selectHeader = document.querySelector('#header');
  if (selectHeader) {
    document.addEventListener('scroll', () => {
      window.scrollY > 100 ? selectHeader.classList.add('header-scrolled') : selectHeader.classList.remove('header-scrolled');
    });
  }

  /**
   * Back to top button
   */
  const backtotop = document.querySelector('.back-to-top');
  if (backtotop) {
    function toggleBacktotop() {
      if (window.scrollY > 100) {
        backtotop.classList.add('active');
      } else {
        backtotop.classList.remove('active');
      }
    }
    window.addEventListener('load', toggleBacktotop);
    document.addEventListener('scroll', toggleBacktotop);
  }

  /**
   * Apply .scrolled class to the body as the page is scrolled down
   */
  function toggleScrolled() {
    const selectBody = document.querySelector('body');
    const selectHeader = document.querySelector('#header');
    if (selectHeader && !selectHeader.classList.contains('scroll-up-sticky') && !selectHeader.classList.contains('sticky-top') && !selectHeader.classList.contains('fixed-top')) return;
    if (selectBody) {
    window.scrollY > 100 ? selectBody.classList.add('scrolled') : selectBody.classList.remove('scrolled');
    }
  }

  document.addEventListener('scroll', toggleScrolled);
  window.addEventListener('load', toggleScrolled);

  /**
   * Mobile nav toggle
   */
  const mobileNavToggleBtn = document.querySelector('.mobile-nav-toggle');

  function mobileNavToogle() {
    const body = document.querySelector('body');
    if (body) {
      // Only toggle mobile nav for screens < 1200px
      if (window.innerWidth < 1200) {
        body.classList.toggle('mobile-nav-active');
      } else {
        body.classList.remove('mobile-nav-active');
      }
    }
    if (mobileNavToggleBtn) {
    mobileNavToggleBtn.classList.toggle('bi-list');
    mobileNavToggleBtn.classList.toggle('bi-x');
    }
  }
  if (mobileNavToggleBtn) {
    mobileNavToggleBtn.addEventListener('click', mobileNavToogle);
  }

  // Always close mobile nav on navigation (Angular router)
  const navmenuLinks = document.querySelectorAll('#navmenu a');
  if (navmenuLinks.length > 0) {
    navmenuLinks.forEach(navmenu => {
    navmenu.addEventListener('click', () => {
        const body = document.querySelector('body');
        if (body && body.classList.contains('mobile-nav-active')) {
          body.classList.remove('mobile-nav-active');
          if (mobileNavToggleBtn) {
            mobileNavToggleBtn.classList.add('bi-list');
            mobileNavToggleBtn.classList.remove('bi-x');
          }
      }
    });
  });
  }

  /**
   * Toggle mobile nav dropdowns
   */
  const navmenuDropdowns = document.querySelectorAll('.navmenu .toggle-dropdown');
  if (navmenuDropdowns.length > 0) {
    navmenuDropdowns.forEach(navmenu => {
    navmenu.addEventListener('click', function(e) {
      e.preventDefault();
        if (this.parentNode) {
      this.parentNode.classList.toggle('active');
        }
        if (this.parentNode && this.parentNode.nextElementSibling) {
      this.parentNode.nextElementSibling.classList.toggle('dropdown-active');
        }
      e.stopImmediatePropagation();
    });
  });
  }

  /**
   * Scroll top button
   */
  let scrollTop = document.querySelector('.scroll-top');

  function toggleScrollTop() {
    if (scrollTop) {
      window.scrollY > 100 ? scrollTop.classList.add('active') : scrollTop.classList.remove('active');
    }
  }
  if (scrollTop) {
  scrollTop.addEventListener('click', (e) => {
    e.preventDefault();
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  });
  }

  window.addEventListener('load', toggleScrollTop);
  document.addEventListener('scroll', toggleScrollTop);

  /**
   * Animation on scroll function and init
   */
  function aosInit() {
    if (typeof AOS !== 'undefined') {
    AOS.init({
      duration: 600,
      easing: 'ease-in-out',
      once: true,
      mirror: false
    });
    }
  }
  window.addEventListener('load', aosInit);

  /**
   * Initiate glightbox
   */
  if (typeof GLightbox !== 'undefined') {
  const glightbox = GLightbox({
    selector: '.glightbox'
  });
  }

  /**
   * Initiate Pure Counter
   */
  if (typeof PureCounter !== 'undefined') {
  new PureCounter();
  }

  /**
   * Frequently Asked Questions Toggle
   */
  const faqItems = document.querySelectorAll('.faq-item h3, .faq-item .faq-toggle');
  if (faqItems.length > 0) {
    faqItems.forEach((faqItem) => {
    faqItem.addEventListener('click', () => {
        if (faqItem.parentNode) {
      faqItem.parentNode.classList.toggle('faq-active');
        }
      });
    });
  }

  /**
   * Init isotope layout and filters
   */
  const isotopeLayouts = document.querySelectorAll('.isotope-layout');
  if (isotopeLayouts.length > 0 && typeof Isotope !== 'undefined' && typeof imagesLoaded !== 'undefined') {
    isotopeLayouts.forEach(function(isotopeItem) {
    let layout = isotopeItem.getAttribute('data-layout') ?? 'masonry';
    let filter = isotopeItem.getAttribute('data-default-filter') ?? '*';
    let sort = isotopeItem.getAttribute('data-sort') ?? 'original-order';

    let initIsotope;
      const isotopeContainer = isotopeItem.querySelector('.isotope-container');
      if (isotopeContainer) {
        imagesLoaded(isotopeContainer, function() {
          initIsotope = new Isotope(isotopeContainer, {
        itemSelector: '.isotope-item',
        layoutMode: layout,
        filter: filter,
        sortBy: sort
      });
    });

        const isotopeFilters = isotopeItem.querySelectorAll('.isotope-filters li');
        if (isotopeFilters.length > 0) {
          isotopeFilters.forEach(function(filters) {
      filters.addEventListener('click', function() {
              const activeFilter = isotopeItem.querySelector('.isotope-filters .filter-active');
              if (activeFilter) {
                activeFilter.classList.remove('filter-active');
              }
        this.classList.add('filter-active');
              if (initIsotope) {
        initIsotope.arrange({
          filter: this.getAttribute('data-filter')
        });
              }
        if (typeof aosInit === 'function') {
          aosInit();
        }
      }, false);
    });
        }
      }
  });
  }

  /**
   * Init swiper sliders
   */
  function initSwiper() {
    if (typeof Swiper !== 'undefined') {
      const swiperElements = document.querySelectorAll(".init-swiper");
      if (swiperElements.length > 0) {
        swiperElements.forEach(function(swiperElement) {
          const swiperConfig = swiperElement.querySelector(".swiper-config");
          if (swiperConfig) {
            let config = JSON.parse(swiperConfig.innerHTML.trim());

      if (swiperElement.classList.contains("swiper-tab")) {
        initSwiperWithCustomPagination(swiperElement, config);
      } else {
        new Swiper(swiperElement, config);
            }
      }
    });
      }
    }
  }

  window.addEventListener("load", initSwiper);

  /**
   * Correct scrolling position upon page load for URLs containing hash links.
   */
  window.addEventListener('load', function(e) {
    if (window.location.hash) {
      const hashElement = document.querySelector(window.location.hash);
      if (hashElement) {
        setTimeout(() => {
          let section = hashElement;
          let scrollMarginTop = getComputedStyle(section).scrollMarginTop;
          window.scrollTo({
            top: section.offsetTop - parseInt(scrollMarginTop),
            behavior: 'smooth'
          });
        }, 100);
      }
    }
  });

  /**
   * Navmenu Scrollspy
   */
  let navmenulinks = document.querySelectorAll('.navmenu a');

  function navmenuScrollspy() {
    navmenulinks.forEach(navmenulink => {
      if (!navmenulink.hash) return;
      let section = document.querySelector(navmenulink.hash);
      if (!section) return;
      let position = window.scrollY + 200;
      if (position >= section.offsetTop && position <= (section.offsetTop + section.offsetHeight)) {
        const activeLinks = document.querySelectorAll('.navmenu a.active');
        if (activeLinks.length > 0) {
          activeLinks.forEach(link => link.classList.remove('active'));
        }
        navmenulink.classList.add('active');
      } else {
        navmenulink.classList.remove('active');
      }
    })
  }
  window.addEventListener('load', navmenuScrollspy);
  document.addEventListener('scroll', navmenuScrollspy);

})();