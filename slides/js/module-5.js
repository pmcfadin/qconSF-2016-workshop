Animation.animate(function () {
  registerCanvas('integration');
  var intega = registerSVG('integration', 'images/courses/DS420/components/addendum/indexing1.svg', 1128, 545, 25, 0);
  var integb = registerSVG('integration', 'images/courses/DS420/components/addendum/indexing2.svg', 1128, 545, 25, 0, 0);
  var integc = registerSVG('integration', 'images/courses/DS420/components/addendum/indexing3.svg', 1128, 545, 25, 0, 0);
  var integd = registerSVG('integration', 'images/courses/DS420/components/addendum/indexing4.svg', 1128, 545, 25, 0, 0);
  var intege = registerSVG('integration', 'images/courses/DS420/components/addendum/indexing5.svg', 1128, 545, 25, 0, 0);
  var cdataa = registerSVG('integration', 'images/courses/DS420/components/addendum/cassandra-data.svg', 39, 50, 288, 268, 0);
  var cdatab = registerSVG('integration', 'images/courses/DS420/components/addendum/cassandra-data.svg', 39, 50, 418, 143, 0);
  var sdata = registerSVG('integration', 'images/courses/DS420/components/addendum/solr-data.svg', 43, 47, 1080, 144, 0);

  addAnimation('integration', function() {
    intega.image.hide();
    integb.image.show();
    cdataa.image.show();
    cdatab.image.show();
  });
  addAnimation('integration', function() {
    integb.image.hide();
    integc.image.show();
    sdata.image.show();
  });
  addAnimation('integration', function() {
    integc.image.hide();
    integd.image.show();
    sdata.image.animate(1000, '<>', 100).move(1075, 289);
  });
  addAnimation('integration', function() {
    integd.image.hide();
    intege.image.show();
    cdataa.image.hide();
    cdatab.image.animate(1000, '<>', 100).move(345, 420);
    sdata.image.animate(1000, '<>', 100).move(1075, 445);
  });
});

Animation.animate(function () {
  registerCanvas('dataindexing');
  var cluster = registerSVG('dataindexing', 'images/courses/DS420/components/addendum/index1.svg', 663, 459, 200, 0);
  var insert = registerSVG('dataindexing', 'images/courses/DS420/components/addendum/index2.svg', 107, 53, 300, 240, 0);
  var forward = registerSVG('dataindexing','images/courses/DS420/components/addendum/index3.svg', 285, 298, 475, 83, 0);
  var sstable = registerSVG('dataindexing','images/courses/DS420/components/addendum/index4.svg', 162, 50, 505, 83, 0);
  var hook = registerSVG('dataindexing','images/courses/DS420/components/addendum/index5.svg', 233, 52, 520, 155, 0);
  var index = registerSVG('dataindexing','images/courses/DS420/components/addendum/index6.svg', 165, 81, 505, 215, 0);

  addAnimation('dataindexing', function() {
    insert.image.show();
  });
  addAnimation('dataindexing', function() {
    forward.image.show();
  });
  addAnimation('dataindexing', function() {
    insert.image.hide();
    forward.image.hide();
    cluster.image.animate(1000, '<>', 100).size(2000, 2000).move(-1500, -800);
  });
  addAnimation('dataindexing', function() {
    sstable.image.show();
  });
  addAnimation('dataindexing', function() {
    hook.image.show();
    index.image.show();
  });
});

Animation.animate(function () {
  registerCanvas('solrquery');
  var querya = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query1.svg', 790, 532, 100, 0);
  var queryb = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query2.svg', 790, 532, 100, 0, 0);
  var queryc = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query5.svg', 790, 532, 100, 0);
  var queryd = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query3.svg', 790, 532, 100, 0, 0);
  var querye = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query4.svg', 790, 532, 100, 0, 0);
  var cdatac = registerSVG('solrquery', 'images/courses/DS420/components/addendum/cassandra-data.svg', 39, 50, 755, 250);
  var cdatad = registerSVG('solrquery', 'images/courses/DS420/components/addendum/cassandra-data.svg', 39, 50, 178, 250);
  var sdataa = registerSVG('solrquery', 'images/courses/DS420/components/addendum/solr-data.svg', 43, 47, 755, 250, 0);
  var sdatab = registerSVG('solrquery', 'images/courses/DS420/components/addendum/solr-data.svg', 43, 47, 178, 250, 0);
  var ida = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query6.svg', 790, 532, 100, 0, 0);
  var idb = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query7.svg', 790, 532, 100, 0, 0);
  var idc = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query8.svg', 790, 532, 100, 0, 0);
  var titles = registerSVG('solrquery', 'images/courses/DS420/components/addendum/query9.svg', 794, 532, 100, 0, 0);

  queryc.image.opacity(0);
  cdatac.image.opacity(0);
  cdatad.image.opacity(0);

  addAnimation('solrquery', function() {
    queryb.image.show();
  });
  addAnimation('solrquery', function() {
    querya.image.hide();
    queryb.image.hide();
    queryd.image.show();
  });
  addAnimation('solrquery', function() {
    ida.image.show();
    idb.image.show();
    sdataa.image.show();
    sdatab.image.show();
  });
  addAnimation('solrquery', function() {
    sdataa.image.animate(1000, '<>', 100).move(495, 150);
    sdatab.image.animate(1000, '<>', 100).move(495, 150);
    ida.image.hide();
    idb.image.hide();
    querye.image.show();
  });
  addAnimation('solrquery', function() {
    idc.image.show();
  });
  addAnimation('solrquery', function() {
    idc.image.hide();
    sdataa.image.animate(1000, '<>', 100).move(755, 250);
    sdatab.image.animate(1000, '<>', 100).move(178, 250);
  });
  addAnimation('solrquery', function() {
    sdataa.image.hide();
    sdatab.image.hide();
    cdatac.image.animate(200, '<>', 0).opacity(1);
    cdatad.image.animate(200, '<>', 0).opacity(1);
    titles.image.show();
  });
  addAnimation('solrquery', function() {
    cdatac.image.animate(1000, '<>', 100).move(495, 150);
    cdatad.image.animate(1000, '<>', 100).move(495, 150);
    titles.image.hide();
  });
  addAnimation('solrquery', function() {
    cdatac.image.hide();
    cdatad.image.animate(1000, '<>', 100).move(755, 50);
  });
});

