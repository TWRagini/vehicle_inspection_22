package com.melayer.vehicleftp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.melayer.vehicleftp.R;
import com.melayer.vehicleftp.activity.MainActivity;
import com.melayer.vehicleftp.database.repository.RepoImplRegisterVehicle;
import com.melayer.vehicleftp.database.repository.RepoRegisterVehicle;
import com.melayer.vehicleftp.domain.VehicleRegistration;
import com.melayer.vehicleftp.prefs.Prefs;
import com.melayer.vehicleftp.temp.InitVehicleObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by root on 22/8/16.
 */
public class CheckListFragment extends Fragment {
    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static final String KEY_BACKWORD_FRAGMENT_DATA = "data";

    public static CheckListFragment newInstance(Map<String,String> forwardData) {
        CheckListFragment checkListFragment = new CheckListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_FRAGMENT_NAME, "CheckListFragment");
        args.putSerializable(KEY_BACKWORD_FRAGMENT_DATA, (Serializable) forwardData);
        checkListFragment.setArguments(args);
        return checkListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_checklist, container, false);
        getParent().hideKeyboard(rootView);
        initSpinnerUnit(rootView);
        final InitVehicleObject vehicleObject = new InitVehicleObject(getParent());

        ArrayList<String> units = new ArrayList<>();
        units.add("Select");
        units.add("Yes");
        units.add("No");

        final Spinner spinnerHelmetCondition = (Spinner)
                rootView.findViewById(R.id.spinnerHelmetCondition);

        if(spinnerHelmetCondition.getSelectedItem()!=null && spinnerHelmetCondition.getSelectedItem().toString().equals("Yes")) {
            vehicleObject.passVehicleObject("" + spinnerHelmetCondition.getTag(), 1);
        }


        final ArrayAdapter<String> adapterHelmetCondition = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, units);
        spinnerHelmetCondition.setAdapter(adapterHelmetCondition);

        spinnerHelmetCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerHelmetCondition.getTag() - "+spinnerHelmetCondition.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerHelmetCondition.getTag(), 1);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerHelmetCondition,adapterHelmetCondition,""+spinnerHelmetCondition.getTag(),1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });



        final Spinner spinnerValidRc = (Spinner)
                rootView.findViewById(R.id.spinnerValidRc);
        vehicleObject.passVehicleObject("" + spinnerValidRc.getTag(), 2);

        ArrayList<String> unitsValidRc = new ArrayList<>();
        unitsValidRc.add("Select");
        unitsValidRc.add("Yes");
        unitsValidRc.add("No");

        final ArrayAdapter<String> adapterValidRc = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsValidRc);
        spinnerValidRc.setAdapter(adapterValidRc);

        spinnerValidRc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerValidRc.getTag() - "+spinnerValidRc.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerValidRc.getTag(), 2);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerValidRc,adapterValidRc,""+spinnerValidRc.getTag(),2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        final Spinner spinnerValidInsurance = (Spinner)
                rootView.findViewById(R.id.spinnerValidInsurance);
        vehicleObject.passVehicleObject("" + spinnerValidInsurance.getTag(), 3);

        ArrayList<String> unitsValidInsurance = new ArrayList<>();
        unitsValidInsurance.add("Select");
        unitsValidInsurance.add("Yes");
        unitsValidInsurance.add("No");

        final ArrayAdapter<String> adapterValidInsurance = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsValidInsurance);
        spinnerValidInsurance.setAdapter(adapterValidInsurance);

        spinnerValidInsurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerValidInsurance.getTag() - "+spinnerValidInsurance.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerValidInsurance.getTag(), 3);
                }
                if(parent.getSelectedItem().equals("No")){
                   vehicleObject.failVehicleObject(spinnerValidInsurance,adapterValidInsurance,""+spinnerValidInsurance.getTag(),3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        final Spinner spinnerPucCertificate = (Spinner)
                rootView.findViewById(R.id.spinnerPucCertificate);
        vehicleObject.passVehicleObject("" + spinnerPucCertificate.getTag(), 4);

        ArrayList<String> unitsPucCertificate = new ArrayList<>();
        unitsPucCertificate.add("Select");
        unitsPucCertificate.add("Yes");
        unitsPucCertificate.add("No");

        final ArrayAdapter<String> adapterPucCertificate = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsPucCertificate);
        spinnerPucCertificate.setAdapter(adapterPucCertificate);

        spinnerPucCertificate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerPucCertificate.getTag() - "+spinnerPucCertificate.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerPucCertificate.getTag(), 4);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerPucCertificate,adapterPucCertificate,""+spinnerPucCertificate.getTag(),4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerFrontTyres = (Spinner)
                rootView.findViewById(R.id.spinnerFrontTyres);
        vehicleObject.passVehicleObject("" + spinnerFrontTyres.getTag(), 5);

        ArrayList<String> unitsFrontTyres = new ArrayList<>();
        unitsFrontTyres.add("Select");
        unitsFrontTyres.add("Yes");
        unitsFrontTyres.add("No");

        final ArrayAdapter<String> adapterFrontTyres = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsFrontTyres);
        spinnerFrontTyres.setAdapter(adapterFrontTyres);

        spinnerFrontTyres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerFrontTyres.getTag() - "+spinnerFrontTyres.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerFrontTyres.getTag(), 5);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerFrontTyres,adapterFrontTyres,""+spinnerFrontTyres.getTag(),5);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerRearTyres = (Spinner)
                rootView.findViewById(R.id.spinnerRearTyres);
        vehicleObject.passVehicleObject("" + spinnerRearTyres.getTag(), 6);

        ArrayList<String> unitsRearTyres = new ArrayList<>();
        unitsRearTyres.add("Select");
        unitsRearTyres.add("Yes");
        unitsRearTyres.add("No");

        final ArrayAdapter<String> adapterRearTyres = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsRearTyres);
        spinnerRearTyres.setAdapter(adapterRearTyres);

        spinnerRearTyres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerRearTyres.getTag() - "+spinnerRearTyres.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerRearTyres.getTag(), 6);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerRearTyres,adapterRearTyres,""+spinnerRearTyres.getTag(),6);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerSpareWheel = (Spinner)
                rootView.findViewById(R.id.spinnerSpareWheel);
        vehicleObject.passVehicleObject("" + spinnerSpareWheel.getTag(), 7);

        ArrayList<String> unitsSpareWheel = new ArrayList<>();
        unitsSpareWheel.add("Select");
        unitsSpareWheel.add("Yes");
        unitsSpareWheel.add("No");

        final ArrayAdapter<String> adapterSpareWheel = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsSpareWheel);
        spinnerSpareWheel.setAdapter(adapterSpareWheel);

        spinnerSpareWheel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerSpareWheel.getTag() - "+spinnerSpareWheel.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerSpareWheel.getTag(), 7);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerSpareWheel,adapterSpareWheel,""+spinnerSpareWheel.getTag(),7);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerWheelNuts = (Spinner)
                rootView.findViewById(R.id.spinnerWheelNuts);
        vehicleObject.passVehicleObject("" + spinnerWheelNuts.getTag(), 8);

        ArrayList<String> unitsWheelNuts = new ArrayList<>();
        unitsWheelNuts.add("Select");
        unitsWheelNuts.add("Yes");
        unitsWheelNuts.add("No");

        final ArrayAdapter<String> adapterWheelNuts = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsWheelNuts);
        spinnerWheelNuts.setAdapter(adapterWheelNuts);

        spinnerWheelNuts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerWheelNuts.getTag() - "+spinnerWheelNuts.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerWheelNuts.getTag(), 8);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerWheelNuts,adapterWheelNuts,""+spinnerWheelNuts.getTag(),8);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerBreakCondition = (Spinner)
                rootView.findViewById(R.id.spinnerBreakCondition);
        vehicleObject.passVehicleObject("" + spinnerBreakCondition.getTag(), 9);

        ArrayList<String> unitsBreakCondition = new ArrayList<>();
        unitsBreakCondition.add("Select");
        unitsBreakCondition.add("Yes");
        unitsBreakCondition.add("No");

        final ArrayAdapter<String> adapterBreakCondition = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsBreakCondition);
        spinnerBreakCondition.setAdapter(adapterBreakCondition);

        spinnerBreakCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerBreakCondition.getTag() - "+spinnerBreakCondition.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerBreakCondition.getTag(), 9);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerBreakCondition,adapterBreakCondition,""+spinnerBreakCondition.getTag(),9);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerLightCondition = (Spinner)
                rootView.findViewById(R.id.spinnerLightCondition);
        vehicleObject.passVehicleObject("" + spinnerLightCondition.getTag(), 10);

        ArrayList<String> unitsLightCondition = new ArrayList<>();
        unitsLightCondition.add("Select");
        unitsLightCondition.add("Yes");
        unitsLightCondition.add("No");

        final ArrayAdapter<String> adapterLightCondition = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsLightCondition);
        spinnerLightCondition.setAdapter(adapterLightCondition);

        spinnerLightCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerLightCondition.getTag() - "+spinnerLightCondition.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerLightCondition.getTag(), 10);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerLightCondition,adapterLightCondition,""+spinnerLightCondition.getTag(),10);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerRearViewMirrors = (Spinner)
                rootView.findViewById(R.id.spinnerRearViewMirrors);
        vehicleObject.passVehicleObject("" + spinnerRearViewMirrors.getTag(), 11);

        ArrayList<String> unitsRearViewMirrors = new ArrayList<>();
        unitsRearViewMirrors.add("Select");
        unitsRearViewMirrors.add("Yes");
        unitsRearViewMirrors.add("No");

        final ArrayAdapter<String> adapterRearViewMirrors = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsRearViewMirrors);
        spinnerRearViewMirrors.setAdapter(adapterRearViewMirrors);

        spinnerRearViewMirrors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerRearViewMirrors.getTag() - "+spinnerRearViewMirrors.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerRearViewMirrors.getTag(), 11);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerRearViewMirrors,adapterRearViewMirrors,""+spinnerRearViewMirrors.getTag(),11);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        final Spinner spinnerSideIndicators = (Spinner)
                rootView.findViewById(R.id.spinnerSideIndicators);
        vehicleObject.passVehicleObject("" + spinnerSideIndicators.getTag(), 12);

        ArrayList<String> unitsSideIndicators = new ArrayList<>();
        unitsSideIndicators.add("Select");
        unitsSideIndicators.add("Yes");
        unitsSideIndicators.add("No");

        final ArrayAdapter<String> adapterSideIndicators = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, unitsSideIndicators);
        spinnerSideIndicators.setAdapter(adapterSideIndicators);

        spinnerSideIndicators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(MainActivity.TAG,"spinnerSideIndicators.getTag() - "+spinnerSideIndicators.getTag());
                if(parent.getSelectedItem().equals("Yes")){
                    vehicleObject.passVehicleObject("" + spinnerSideIndicators.getTag(), 12);
                }
                if(parent.getSelectedItem().equals("No")){
                    vehicleObject.failVehicleObject(spinnerSideIndicators,adapterSideIndicators,""+spinnerSideIndicators.getTag(),12);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        Log.i(MainActivity.TAG,"vehicleObject total- "+vehicleObject.toString());
        rootView.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParent().popBackStack(1);
            }
        });
        rootView.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerHelmetCondition.getSelectedItem().equals("Select") || spinnerValidRc.getSelectedItem().equals("Select")||
                spinnerValidInsurance.getSelectedItem().equals("Select") || spinnerPucCertificate.getSelectedItem().equals("Select")||
                spinnerFrontTyres.getSelectedItem().equals("Select")|| spinnerRearTyres.getSelectedItem().equals("Select")||
                spinnerSpareWheel.getSelectedItem().equals("Select")|| spinnerWheelNuts.getSelectedItem().equals("Select")||
                spinnerBreakCondition.getSelectedItem().equals("Select")|| spinnerLightCondition.getSelectedItem().equals("Select")||
                spinnerRearViewMirrors.getSelectedItem().equals("Select") || spinnerSideIndicators.getSelectedItem().equals("Select")){
                    getParent().snack(rootView,"Please Complete All the Checkpoints!!");
                }else {

                    vehicleObject.buildAlertMessage(rootView,saveVehicleLocally());

                }
            }
        });
        return rootView;
    }
    private VehicleRegistration saveVehicleLocally() {
        Log.i(MainActivity.TAG,"KEY_BACKWORD_FRAGMENT_DATA - "+getArguments().get(KEY_BACKWORD_FRAGMENT_DATA));
        Map<String,String> collectData = (Map<String, String>) getArguments().getSerializable(KEY_BACKWORD_FRAGMENT_DATA);
        Log.i(MainActivity.TAG,"collectData - "+collectData.toString());

        RepoRegisterVehicle repoRegisterVehicle = new RepoImplRegisterVehicle(getParent().getDbHelper());
        VehicleRegistration vehicleRegistration = new VehicleRegistration();
        try {
            vehicleRegistration.setVehicleId(collectData.get("vehicleId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        vehicleRegistration.setVehicleNo(collectData.get("vehicleNo"));
        vehicleRegistration.setPsrName(collectData.get("PsrName"));
        vehicleRegistration.setCellNumber(collectData.get("CellNumber"));
        vehicleRegistration.setStartPlace(collectData.get("StartPlace"));
        vehicleRegistration.setEndPlace(collectData.get("EndPlace"));
        vehicleRegistration.setLicenceNo(collectData.get("LicenseNo"));
        vehicleRegistration.setLicenceValidity(collectData.get("LicenseValidity"));
        vehicleRegistration.setTdmAsmName(collectData.get("TdmAsmName"));
        vehicleRegistration.setTdmAsm(collectData.get("TdmAsm"));
        vehicleRegistration.setDistributor(collectData.get("Distributor"));
        try {
            vehicleRegistration.setUserId(collectData.get("UserId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        vehicleRegistration.setEntryDate(collectData.get("date"));
        Log.i(MainActivity.TAG,"Vehicle Object :" +vehicleRegistration.toString());
        //repoRegisterVehicle.registerVehicle(vehicleRegistration);
    return  vehicleRegistration;
    }


    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }

    private void initSpinnerUnit(final View rootView) {
        Spinner spinnerUnit = (Spinner) rootView.findViewById(R.id.spinnerUnit);
        ArrayList<String> units = new ArrayList<>();
        units.add("North");
        units.add("East");
        units.add("GMRC (Gujarat,MP,Rajasthan,Chhattisgarh)");
        units.add("M&G (Maharashtra & Goa)");
        units.add("APTK (AP,Telangana,Karnataka)");
        units.add("TNK (Tamilnadu, Kerala)");
        units.add("Agro North");
        units.add("Agro West");
        units.add("Agro East");
        units.add("Nutrition");
        units.add("HQ");
        Collections.sort(units);
        ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnit.setAdapter(adapterUnit);
        Prefs.saveUnitName(getContext(), ((Spinner) rootView.findViewById(R.id.spinnerUnit)).getSelectedItem().toString());
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Log.i(MainActivity.TAG,"Selected Unit : "+parent.getSelectedItem().toString());
                Prefs.saveUnitName(getContext(), parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}